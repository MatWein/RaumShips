package com.mw.raumships.common.blocks.rings;

import com.mw.raumships.client.gui.rings.ILinkable;
import com.mw.raumships.client.rendering.rings.RingsControllerRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RingsControllerTile extends TileEntity implements ILinkable {
    private BlockPos linkedRings;
    private RingsControllerRenderer renderer;

    public void setLinkedRings(BlockPos pos) {
        this.linkedRings = pos;

        markDirty();
    }

    public BlockPos getLinkedRings() {
        return linkedRings;
    }

    @Override
    public boolean isLinked() {
        return linkedRings != null;
    }

    public RingsTile getLinkedRingsTile(World world) {
        return (linkedRings != null ? ((RingsTile) world.getTileEntity(linkedRings)) : null);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if (linkedRings != null)
            compound.setLong("linkedRings", linkedRings.toLong());

        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("linkedRings"))
            linkedRings = BlockPos.fromLong(compound.getLong("linkedRings"));

        super.readFromNBT(compound);
    }

    public RingsControllerRenderer getRenderer() {
        if (renderer == null)
            renderer = new RingsControllerRenderer(this);

        return renderer;
    }
}
