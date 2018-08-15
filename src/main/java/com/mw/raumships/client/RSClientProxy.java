package com.mw.raumships.client;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.common.PuddleJumperEntity;
import com.mw.raumships.common.RSCommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PatchedEntityRenderer;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class RSClientProxy extends RSCommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) throws Exception {
        super.preInit(event);

        OBJLoader.INSTANCE.addDomain(RaumShipsMod.MODID);

        RenderingRegistry.registerEntityRenderingHandler(PuddleJumperEntity.class, PuddleJumperRenderer::new);

        Keybinds.init();
    }

    @Override
    public void load(FMLInitializationEvent event) throws Exception {
        super.load(event);

        PatchedEntityRenderer patchedEntityRenderer = new PatchedEntityRenderer(
                Minecraft.getMinecraft(),
                Minecraft.getMinecraft().getResourceManager());

        patchedEntityRenderer.setThirdPersonDistance(8.0F);

        Minecraft.getMinecraft().entityRenderer = patchedEntityRenderer;
    }
}
