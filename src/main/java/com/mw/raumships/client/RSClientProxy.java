package com.mw.raumships.client;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.rendering.EntityWithModelRenderer;
import com.mw.raumships.client.rendering.rings.RingsControllerTESR;
import com.mw.raumships.client.rendering.rings.RingsTESR;
import com.mw.raumships.common.RSCommonProxy;
import com.mw.raumships.common.blocks.rings.RingsControllerTile;
import com.mw.raumships.common.blocks.rings.RingsTile;
import com.mw.raumships.common.entities.*;
import net.minecraft.client.renderer.PatchedEntityRenderer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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
        RenderingRegistry.registerEntityRenderingHandler(F301Entity.class, EntityWithModelRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(F302Entity.class, EntityWithModelRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(GateGliderEntity.class, EntityWithModelRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TeleakEntity.class, EntityWithModelRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(X304Entity.class, EntityWithModelRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(HatakEntity.class, EntityWithModelRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AtlantisEntity.class, EntityWithModelRenderer::new);

        ClientRegistry.bindTileEntitySpecialRenderer(RingsTile.class, new RingsTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(RingsControllerTile.class, new RingsControllerTESR());

        Keybinds.init();

        setCustomModelResourceLocation(zpmItem, 0, "zpm/1");
        setCustomModelResourceLocation(zpmItem, 1, "zpm/2");
        setCustomModelResourceLocation(zpmItem, 2, "zpm/3");
        setCustomModelResourceLocation(zpmItem, 3, "zpm/4");
        setCustomModelResourceLocation(zpmItem, 4, "zpm/5");
        setCustomModelResourceLocation(zpmItem, 5, "zpm/6");
        setCustomModelResourceLocation(zpmItem, 6, "zpm/7");
        setCustomModelResourceLocation(zpmItem, 7, "zpm/8");
        setCustomModelResourceLocation(zpmHubItem, "zpmhub");
        setCustomModelResourceLocation(zpmChargerItem, "zpmcharger");
        setCustomModelResourceLocation(ringsItem, "rings_block");
        setCustomModelResourceLocation(analyzerAncientItem, "analyzer_ancient");
        setCustomModelResourceLocation(ringsControllerItem, "rings_controller_block");
    }

    private void setCustomModelResourceLocation(Item item, String name) {
        setCustomModelResourceLocation(item, 0, name);
    }

    private void setCustomModelResourceLocation(Item item, int metadata, String name) {
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(RaumShipsMod.MODID + ":" + name, "inventory"));
    }

    @Override
    public void load(FMLInitializationEvent event) throws Exception {
        super.load(event);

        getMc().entityRenderer = new PatchedEntityRenderer(getMc(), getMc().getResourceManager());
    }

    @Override
    public EntityPlayer getPlayerClientSide() {
        return getMc().player;
    }

    @Override
    public void addScheduledTaskClientSide(Runnable runnable) {
        getMc().addScheduledTask(runnable);
    }
}
