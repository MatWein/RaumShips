package com.mw.raumships.common.blocks;

import com.mw.raumships.common.items.ZPMItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

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

        if (hasZpm && (getEnergyStored() < getMaxEnergyStored())) {
            for (EnumFacing enumFacing : EnumFacing.values()) {
                if (enumFacing == EnumFacing.UP) {
                    continue;
                }

                BlockPos neighborBlock = pos.offset(enumFacing);
                TileEntity tile = world.getTileEntity(neighborBlock);
                if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, enumFacing.getOpposite())) {
                    int maxReceive = getMaxEnergyStored() - getEnergyStored();
                    int maxPossible = receiveEnergy(maxReceive, true);

                    IEnergyStorage capability = tile.getCapability(CapabilityEnergy.ENERGY, enumFacing.getOpposite());
                    if (capability != null && capability.canExtract() && this.canReceive()) {
                        int energyTransferred = capability.extractEnergy(maxPossible, false);
                        receiveEnergy(energyTransferred, false);
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return ID;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        ItemStack stackInSlot = getStackInSlot(0);
        if (stackInSlot == null) {
            return 0;
        }

        int energyStored = getEnergyStored();
        int maxEnergyStored = getMaxEnergyStored();
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
        return maxEnergyStored - energyStored;
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
