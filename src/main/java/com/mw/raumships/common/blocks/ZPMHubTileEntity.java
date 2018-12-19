package com.mw.raumships.common.blocks;

import com.mw.raumships.common.items.ZPMItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class ZPMHubTileEntity extends OneSlotEnergyTileEntityBase {
    public static final String ID = "container.zpmhub.name";

    private boolean hasZpmCached;

    @Override
    public void update() {
        boolean hasZpm = hasActiveZpm();
        if (hasZpmCached != hasZpm) {
            hasZpmCached = hasZpm;

            IBlockState blockstate = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, blockstate, blockstate, 3);

            world.checkLightFor(EnumSkyBlock.BLOCK, pos);
        }

        if (getEnergyStoredAsLong() > 0) {
            for (EnumFacing enumFacing : EnumFacing.values()) {
                if (enumFacing == EnumFacing.UP) {
                    continue;
                }

                BlockPos neighborBlock = pos.offset(enumFacing);
                TileEntity tile = world.getTileEntity(neighborBlock);
                if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, enumFacing.getOpposite())) {
                    int maxExtract = getEnergyStored();
                    int maxAvailable = extractEnergy(maxExtract, true);

                    IEnergyStorage capability = tile.getCapability(CapabilityEnergy.ENERGY, enumFacing.getOpposite());
                    if (capability != null && capability.canReceive() && this.canExtract()) {
                        int energyTransferred = capability.receiveEnergy(maxAvailable, false);
                        extractEnergy(energyTransferred, false);
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return ID;
    }

    public boolean hasActiveZpm() {
        return hasZpm() && ZPMItem.getZpmEnergy(getStackInSlot(0)) > 0L;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        ItemStack stackInSlot = getStackInSlot(0);
        if (stackInSlot == null) {
            return 0;
        }

        long energyStored = getEnergyStoredAsLong();
        if (energyStored >= maxExtract) {
            if (!simulate) {
                ZPMItem.setZpmEnergy(stackInSlot, energyStored - maxExtract);
            }
            return maxExtract;
        }

        if (!simulate) {
            ZPMItem.setZpmEnergy(stackInSlot, 0);
        }
        return ZPMItem.toSafeInt(maxExtract - energyStored);
    }

    @Override
    public boolean canExtract() {
        return hasActiveZpm();
    }

    @Override
    public boolean canReceive() {
        return false;
    }
}
