package com.mw.raumships.client.gui.event;

import com.mw.raumships.common.blocks.rings.RingsTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public class PlayerFadeOutRenderEvent {
    private static World world;

    private static long tickStart;
    private static boolean fadeOut;

    public static double calcFog(World world, long tickStart, double partialTicks) {
        double effTick = world.getTotalWorldTime() - tickStart + partialTicks;
        return -(effTick * (effTick - RingsTile.fadeOutTotalTime)) / (20 * 20);
    }

    @SubscribeEvent
    public static void onDrawGui(RenderGameOverlayEvent.Post event) {
        if (fadeOut) {
            float fog = (float) calcFog(world, tickStart, event.getPartialTicks());

            if (fog < 0) {
                fadeOut = false;
            } else {
                int alpha = (int) (fog * 255);

                alpha /= 3;
                alpha <<= 24;

                ScaledResolution res = event.getResolution();
                Gui.drawRect(0, 0, res.getScaledWidth(), res.getScaledHeight(), 0xFFFFFF | alpha);
            }
        }
    }

    public static void startFadeOut() {
        world = Minecraft.getMinecraft().world;
        tickStart = world.getTotalWorldTime();

        fadeOut = true;
    }
}
