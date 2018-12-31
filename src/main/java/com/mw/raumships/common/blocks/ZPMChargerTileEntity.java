package com.mw.raumships.common.blocks;

import com.mw.raumships.common.items.ZPMItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class ZPMChargerTileEntity extends OneSlotEnergyTileEntityBase {
    public static final String ID = "container.zpmcharger.name";

    private boolean hasZpmCached;

    @Override
    public void update() {
        boolean hasZpm = hasZpm();
        if (hasZpmCached != hasZpm) {
            hasZpmCached = hasZpm;

            IBlockState blockstate = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, blockstate, blockstate, 3);
        }
    }

    @Override
    public String getName() {
        return ID;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!hasZpm()) {
            return 0;
        }

        ItemStack stackInSlot = getStackInSlot(0);
        if (stackInSlot == null) {
            return 0;
        }

        long energyStored = getEnergyStoredAsLong();
        long maxEnergyStored = getMaxEnergyStoredAsLong();
        if (energyStored == maxEnergyStored) {
            return 0;
        }

        if (energyStored + maxReceive <= maxEnergyStored) {
            if (!simulate) {
                ZPMItem.setZpmEnergy(stackInSlot, energyStored + maxReceive);
            }
            return maxReceive;
        }

        if (!simulate) {
            ZPMItem.setZpmEnergy(stackInSlot, maxEnergyStored);
        }
        return ZPMItem.toSafeInt(maxEnergyStored - energyStored);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }
}
