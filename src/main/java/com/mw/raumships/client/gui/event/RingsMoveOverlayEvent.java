package com.mw.raumships.client.gui.event;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.ClientUtils;
import com.mw.raumships.client.Keybinds;
import com.mw.raumships.client.gui.rings.GuiBase;
import com.mw.raumships.client.gui.rings.RingsGUI;
import com.mw.raumships.common.blocks.rings.DtoRingsModel;
import com.mw.raumships.common.blocks.rings.RingsBlock;
import com.mw.raumships.common.blocks.rings.RingsTile;
import com.mw.raumships.server.network.RingsControllerActivatedToServer;
import net.minecraft.block.Block;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.Collection;

@EventBusSubscriber(Side.CLIENT)
public class RingsMoveOverlayEvent {
    public static final int OVERLAY_WIDTH = 250;
    public static final int OVERLAY_HEIGHT = 25;
    public static final float OVERLAY_X = 20F;
    public static final float OVERLAY_Y = 200F;

    private static boolean showOverlay = false;
    private static BlockPos ringBlockPos;

    @SubscribeEvent
    public static void makePath(LivingUpdateEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            World world = player.getEntityWorld();

            ringBlockPos = new BlockPos(player.posX, player.posY - 1, player.posZ);
            Block block = world.getBlockState(ringBlockPos).getBlock();

            showOverlay = block instanceof RingsBlock;
        }
    }

    @SubscribeEvent
    public static void onDrawGui(RenderGameOverlayEvent.Post event) {
        if (!showOverlay) {
            return;
        }

        WorldClient world = ClientUtils.getMc().world;
        RingsTile targetRingsTile = (RingsTile) world.getTileEntity(ringBlockPos);
        Collection<DtoRingsModel> connectedRings = targetRingsTile != null && targetRingsTile.ringsMap != null ? targetRingsTile.ringsMap.values() : new ArrayList<>();

        GlStateManager.pushMatrix();
        GlStateManager.translate(OVERLAY_X, OVERLAY_Y, 0F);

        int menuHeight = Math.max(OVERLAY_HEIGHT, OVERLAY_HEIGHT * connectedRings.size());

        Gui.drawRect(0, 0, OVERLAY_WIDTH, menuHeight, GuiBase.BG_COLOR);
        GuiBase.frame(OVERLAY_WIDTH, menuHeight, RingsGUI.FRAME_THICKNESS, GuiBase.FRAME_COLOR);

        int x = RingsGUI.PADDING + 5;
        int y = RingsGUI.PADDING + 1;

        if (connectedRings.isEmpty()) {
            ClientUtils.getMc().fontRenderer.drawStringWithShadow(I18n.format("tile.rings_block.overlay.noConnectedRings"), x, y, GuiBase.TEXT_COLOR);
        } else {
            for (DtoRingsModel connectedRing : connectedRings) {
                ClientUtils.getMc().fontRenderer.drawStringWithShadow(connectedRing.getAddress() + ": " + connectedRing.getName(), x, y, GuiBase.TEXT_COLOR);
                y += OVERLAY_HEIGHT - 5;
            }
        }

        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public static void onKeyInput(KeyInputEvent event) {
        if (showOverlay) {
            if (Keybinds.number1Ctrl.isKeyDown()) {
                activateRingsOnKey(event, 1);
            } else if (Keybinds.number2Ctrl.isKeyDown()) {
                activateRingsOnKey(event, 2);
            } else if (Keybinds.number3Ctrl.isKeyDown()) {
                activateRingsOnKey(event, 3);
            } else if (Keybinds.number4Ctrl.isKeyDown()) {
                activateRingsOnKey(event, 4);
            } else if (Keybinds.number5Ctrl.isKeyDown()) {
                activateRingsOnKey(event, 5);
            } else if (Keybinds.number6Ctrl.isKeyDown()) {
                activateRingsOnKey(event, 6);
            }
        }
    }

    private static void activateRingsOnKey(KeyInputEvent event, int buttonNumber) {
        RingsControllerActivatedToServer message = new RingsControllerActivatedToServer(ringBlockPos, buttonNumber);
        RaumShipsMod.proxy.getNetworkWrapper().sendToServer(message);

        if (event.isCancelable()) {
            event.setCanceled(true);
        }
    }
}
