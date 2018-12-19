package com.mw.raumships.common.blocks;

import com.mw.raumships.common.items.ZPMItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.Arrays;

public abstract class OneSlotEnergyTileEntityBase extends TileEntity implements IInventory, ITickable, IEnergyStorage {
    private static final int TOTAL_SLOTS_COUNT = 1;

    private static final double X_CENTRE_OFFSET = 0.5;
    private static final double Y_CENTRE_OFFSET = 0.5;
    private static final double Z_CENTRE_OFFSET = 0.5;
    private static final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;

    private ItemStack[] itemStacks;

    public OneSlotEnergyTileEntityBase() {
        itemStacks = new ItemStack[TOTAL_SLOTS_COUNT];
        clear();
    }

    @Override
    public int getSizeInventory() {
        return itemStacks.length;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : itemStacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return itemStacks[index];
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int count) {
        ItemStack itemStackInSlot = getStackInSlot(slotIndex);
        if (itemStackInSlot.isEmpty()) return ItemStack.EMPTY;

        ItemStack itemStackRemoved;
        if (itemStackInSlot.getCount() <= count) {
            itemStackRemoved = itemStackInSlot;
            setInventorySlotContents(slotIndex, ItemStack.EMPTY);
        } else {
            itemStackRemoved = itemStackInSlot.splitStack(count);
            if (itemStackInSlot.getCount() == 0) {
                setInventorySlotContents(slotIndex, ItemStack.EMPTY);
            }
        }
        markDirty();
        return itemStackRemoved;
    }

    @Override
    public ItemStack removeStackFromSlot(int slotIndex) {
        ItemStack itemStack = getStackInSlot(slotIndex);
        if (!itemStack.isEmpty()) setInventorySlotContents(slotIndex, ItemStack.EMPTY);
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemstack) {
        itemStacks[slotIndex] = itemstack;
        if (!itemstack.isEmpty() && itemstack.getCount() > getInventoryStackLimit()) {
            itemstack.setCount(getInventoryStackLimit());
        }
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void openInventory(EntityPlayer player) { }

    @Override
    public void closeInventory(EntityPlayer player) { }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem() instanceof ZPMItem;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        Arrays.fill(itemStacks, ItemStack.EMPTY);
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        }

        return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound parentNBTTagCompound) {
        super.writeToNBT(parentNBTTagCompound);

        NBTTagList dataForAllSlots = new NBTTagList();
        for (int i = 0; i < this.itemStacks.length; ++i) {
            if (!this.itemStacks[i].isEmpty()) {  //isEmpty()
                NBTTagCompound dataForThisSlot = new NBTTagCompound();
                dataForThisSlot.setByte("Slot", (byte) i);
                this.itemStacks[i].writeToNBT(dataForThisSlot);
                dataForAllSlots.appendTag(dataForThisSlot);
            }
        }

        parentNBTTagCompound.setTag("Items", dataForAllSlots);
        return parentNBTTagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        final byte NBT_TYPE_COMPOUND = 10;
        NBTTagList dataForAllSlots = nbtTagCompound.getTagList("Items", NBT_TYPE_COMPOUND);

        Arrays.fill(itemStacks, ItemStack.EMPTY);
        for (int i = 0; i < dataForAllSlots.tagCount(); ++i) {
            NBTTagCompound dataForOneSlot = dataForAllSlots.getCompoundTagAt(i);
            byte slotNumber = dataForOneSlot.getByte("Slot");
            if (slotNumber >= 0 && slotNumber < this.itemStacks.length) {
                this.itemStacks[slotNumber] = new ItemStack(dataForOneSlot);
            }
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound updateTagDescribingTileEntityState = getUpdateTag();
        final int METADATA = 0;
        return new SPacketUpdateTileEntity(this.pos, METADATA, updateTagDescribingTileEntityState);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound updateTagDescribingTileEntityState = pkt.getNbtCompound();
        handleUpdateTag(updateTagDescribingTileEntityState);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (facing != EnumFacing.UP && capability == CapabilityEnergy.ENERGY) {
            return true;
        }

        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (facing != EnumFacing.UP && capability == CapabilityEnergy.ENERGY) {
            return (T)this;
        }

        return super.getCapability(capability, facing);
    }

    public boolean hasZpm() {
        ItemStack stackInSlot = getStackInSlot(0);
        if (stackInSlot == null) {
            return false;
        }

        return stackInSlot.getItem() instanceof ZPMItem;
    }

    @Override
    public int getEnergyStored() {
        ItemStack stackInSlot = getStackInSlot(0);
        if (stackInSlot == null) {
            return 0;
        }

        return ZPMItem.getZpmEnergyAsInt(stackInSlot);
    }

    public long getEnergyStoredAsLong() {
        ItemStack stackInSlot = getStackInSlot(0);
        if (stackInSlot == null) {
            return 0;
        }

        return ZPMItem.getZpmEnergy(stackInSlot);
    }

    @Override
    public int getMaxEnergyStored() {
        return ZPMItem.toSafeInt(ZPMItem.MAX_ZPM_ENERGY);
    }

    public long getMaxEnergyStoredAsLong() {
        return ZPMItem.MAX_ZPM_ENERGY;
    }
}
