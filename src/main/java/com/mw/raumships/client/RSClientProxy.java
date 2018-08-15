package com.mw.raumships.client;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.rendering.EntityWithModelRenderer;
import com.mw.raumships.common.RSCommonProxy;
import com.mw.raumships.common.entities.PuddleJumperEntity;
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

        RenderingRegistry.registerEntityRenderingHandler(PuddleJumperEntity.class, EntityWithModelRenderer::new);

        Keybinds.init();
    }

    @Override
    public void load(FMLInitializationEvent event) throws Exception {
        super.load(event);

        PatchedEntityRenderer patchedEntityRenderer = new PatchedEntityRenderer(
                RaumShipsMod.mc,
                RaumShipsMod.mc.getResourceManager());

        patchedEntityRenderer.setThirdPersonDistance(8.0F);

        RaumShipsMod.mc.entityRenderer = patchedEntityRenderer;
    }
}
