package com.mw.raumships.client.rendering.rings;

import com.mw.raumships.common.blocks.rings.RingsControllerTile;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class RingsControllerTESR extends TileEntitySpecialRenderer<RingsControllerTile> {
    @Override
    public void render(RingsControllerTile te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        te.getRenderer().render(x, y, z, partialTicks);
    }
}
