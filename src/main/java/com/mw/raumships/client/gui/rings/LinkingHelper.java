package com.mw.raumships.client.gui.rings;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class LinkingHelper {
    @Nullable
    public static BlockPos findClosestUnlinked(World world, BlockPos startPos, BlockPos radius, Class<? extends Block> targetBlock) {
        double closestDistance = Double.MAX_VALUE;
        BlockPos closest = null;

        for (BlockPos target : BlockPos.getAllInBoxMutable(startPos.subtract(radius), startPos.add(radius))) {
            Block block = world.getBlockState(target).getBlock();

            if (block != null && targetBlock.equals(block.getClass())) {

                ILinkable linkedTile = (ILinkable) world.getTileEntity(target);

                if (!linkedTile.isLinked()) {
                    double distanceSq = startPos.distanceSq(target);

                    if (distanceSq < closestDistance) {
                        closestDistance = distanceSq;
                        closest = target.toImmutable();
                    }
                }
            }
        }

        return closest;
    }
}
