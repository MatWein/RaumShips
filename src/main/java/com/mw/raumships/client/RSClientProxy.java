package com.mw.raumships.client;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.rendering.EntityWithModelRenderer;
import com.mw.raumships.common.RSCommonProxy;
import com.mw.raumships.common.entities.DeathGliderEntity;
import com.mw.raumships.common.entities.PuddleJumperEntity;
import net.minecraft.client.renderer.PatchedEntityRenderer;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static com.mw.raumships.RaumShipsMod.getMc;

public class RSClientProxy extends RSCommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) throws Exception {
        super.preInit(event);

        OBJLoader.INSTANCE.addDomain(RaumShipsMod.MODID);

        RenderingRegistry.registerEntityRenderingHandler(PuddleJumperEntity.class, EntityWithModelRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(DeathGliderEntity.class, EntityWithModelRenderer::new);

        Keybinds.init();
    }

    @Override
    public void load(FMLInitializationEvent event) throws Exception {
        super.load(event);

        getMc().entityRenderer = new PatchedEntityRenderer(getMc(), getMc().getResourceManager());
    }
}
