package com.mw.raumships.client.rendering.rings;

import com.mw.raumships.common.blocks.rings.RingsTile;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RingsRenderer {
    public static final int ringCount = 5;
    public static final int uprisingInterval = 5;
    public static final int fallingInterval = 5;

    public static final double animationDiv = 2.7;

    private World world;
    private List<SingleRingRenderer> rings;

    public RingsRenderer(RingsTile te) {
        this.world = te.getWorld();

        rings = new ArrayList<>();
        for (int i = 0; i < ringCount; i++) {
            rings.add(new SingleRingRenderer(world, i));
        }
    }

    private int currentRing;
    private int lastRingAnimated;
    private long lastTick;

    public int which = 0;

    public void render(double x, double y, double z, double partialTicks) {
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 15 * 16, 15 * 16);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 1.1, z);

        for (SingleRingRenderer ring : rings)
            ring.render(partialTicks);

        GlStateManager.popMatrix();

        long tick = world.getTotalWorldTime() - state.animationStart;

        if (state.isAnimationActive) {
            if (state.ringsUprising) {
                if (tick > 30) {
                    tick -= 30;

                    if (tick % uprisingInterval == 0 && tick != lastTick) {
                        currentRing = (int) (tick / uprisingInterval) - 1;

                        for (int ring = lastRingAnimated + 1; ring < Math.min(currentRing, ringCount); ring++) {
                            rings.get(ring).setTop();
                        }

                        if (currentRing < ringCount) {
                            rings.get(currentRing).animate(state.ringsUprising);

                            lastRingAnimated = currentRing;
                            lastTick = tick;
                        }

                        if (currentRing >= ringCount - 1) {
                            state.ringsUprising = false;

                            lastRingAnimated = ringCount;
                            lastTick = -1;
                        }
                    }
                }
            } else {
                if (tick > 100) {
                    tick -= 100;

                    if (tick % fallingInterval == 0 && tick != lastTick) {
                        currentRing = ringCount - (int) (tick / fallingInterval);

                        for (int ring = lastRingAnimated - 1; ring > Math.max(currentRing, -1); ring--) {
                            rings.get(ring).setDown();
                        }


                        if (currentRing >= 0) {
                            rings.get(currentRing).animate(state.ringsUprising);

                            lastRingAnimated = currentRing;
                            lastTick = tick;
                        } else {
                            state.isAnimationActive = false;
                        }

                        lastTick = tick;
                    }
                }
            }
        }
    }

    public void animationStart(long animationStart) {
        lastTick = -1;
        currentRing = 0;
        lastRingAnimated = -1;

        state.animationStart = animationStart;
        state.ringsUprising = true;
        state.isAnimationActive = true;
    }

    RingsRendererState state = new RingsRendererState();

    public void setState(RingsRendererState rendererState) {
        lastTick = -1;
        this.state = rendererState;
    }
}
