package com.mw.raumships.client.rendering.rings;

import com.mw.raumships.common.blocks.rings.RingsTile;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TransportRingsTESR extends TileEntitySpecialRenderer<RingsTile> {
    @Override
    public void render(RingsTile te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        x += 0.50;
        y += 0.63271 / 2;
        z += 0.50;

        ((ITileEntityRendered) te).getRenderer().render(x, y, z, partialTicks);
    }
}
