package com.mw.raumships.client;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.rendering.EntityWithModelRenderer;
import com.mw.raumships.common.RSCommonProxy;
import com.mw.raumships.common.entities.AlKeshEntity;
import com.mw.raumships.common.entities.DeathGliderEntity;
import com.mw.raumships.common.entities.PuddleJumperEntity;
import net.minecraft.client.renderer.PatchedEntityRenderer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static com.mw.raumships.client.ClientUtils.getMc;

public class RSClientProxy extends RSCommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) throws Exception {
        super.preInit(event);

        OBJLoader.INSTANCE.addDomain(RaumShipsMod.MODID);

        RenderingRegistry.registerEntityRenderingHandler(PuddleJumperEntity.class, EntityWithModelRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(DeathGliderEntity.class, EntityWithModelRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AlKeshEntity.class, EntityWithModelRenderer::new);

        Keybinds.init();

        ModelLoader.setCustomModelResourceLocation(zpmItem, 0, new ModelResourceLocation(RaumShipsMod.MODID + ":zpm/1", "inventory"));
        ModelLoader.setCustomModelResourceLocation(zpmItem, 1, new ModelResourceLocation(RaumShipsMod.MODID + ":zpm/2", "inventory"));
        ModelLoader.setCustomModelResourceLocation(zpmItem, 2, new ModelResourceLocation(RaumShipsMod.MODID + ":zpm/3", "inventory"));
        ModelLoader.setCustomModelResourceLocation(zpmItem, 3, new ModelResourceLocation(RaumShipsMod.MODID + ":zpm/4", "inventory"));
        ModelLoader.setCustomModelResourceLocation(zpmItem, 4, new ModelResourceLocation(RaumShipsMod.MODID + ":zpm/5", "inventory"));
        ModelLoader.setCustomModelResourceLocation(zpmItem, 5, new ModelResourceLocation(RaumShipsMod.MODID + ":zpm/6", "inventory"));
        ModelLoader.setCustomModelResourceLocation(zpmItem, 6, new ModelResourceLocation(RaumShipsMod.MODID + ":zpm/7", "inventory"));
        ModelLoader.setCustomModelResourceLocation(zpmItem, 7, new ModelResourceLocation(RaumShipsMod.MODID + ":zpm/8", "inventory"));

        ModelLoader.setCustomModelResourceLocation(zpmHubItem, 0, new ModelResourceLocation(RaumShipsMod.MODID + ":zpmhub", "inventory"));
    }

    @Override
    public void load(FMLInitializationEvent event) throws Exception {
        super.load(event);

        getMc().entityRenderer = new PatchedEntityRenderer(getMc(), getMc().getResourceManager());
    }
}
