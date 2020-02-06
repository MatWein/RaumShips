package com.mw.raumships.common;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.network.*;
import com.mw.raumships.client.network.RendererUpdatePacketToClient.TileUpdateClientHandler;
import com.mw.raumships.client.network.RingsControllerActivatedToClient.RingsControllerActivatedToClientHandler;
import com.mw.raumships.client.network.StartPlayerFadeOutToClient.StartPlayerFadeOutToClientHandler;
import com.mw.raumships.client.network.StartRingsAnimationToClient.StartRingsAnimationToClientHandler;
import com.mw.raumships.client.network.StateUpdatePacketToClient.StateUpdateClientHandler;
import com.mw.raumships.common.blocks.ZPMChargerBlock;
import com.mw.raumships.common.blocks.ZPMChargerTileEntity;
import com.mw.raumships.common.blocks.ZPMHubBlock;
import com.mw.raumships.common.blocks.ZPMHubTileEntity;
import com.mw.raumships.common.blocks.rings.*;
import com.mw.raumships.common.entities.*;
import com.mw.raumships.common.gui.RaumshipsGuiHandler;
import com.mw.raumships.common.items.AnalyzerAncientItem;
import com.mw.raumships.common.items.ZPMItem;
import com.mw.raumships.server.network.RendererUpdateRequestToServer;
import com.mw.raumships.server.network.RendererUpdateRequestToServer.TileUpdateServerHandler;
import com.mw.raumships.server.network.RingsControllerActivatedToServer;
import com.mw.raumships.server.network.SaveRingsParametersToServer;
import com.mw.raumships.server.network.SaveRingsParametersToServer.SaveRingsParametersServerHandler;
import com.mw.raumships.server.network.StateUpdateRequestToServer;
import com.mw.raumships.server.network.StateUpdateRequestToServer.StateUpdateServerHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.awt.*;

import static com.mw.raumships.RaumShipsMod.MODID;

public abstract class RSCommonProxy {
    protected RaumshipsItemTab raumshipsItemTab;
    protected ZPMHubBlock zpmHubBlock;
    protected Item zpmHubItem;
    protected ZPMItem zpmItem;
    protected ZPMChargerBlock zpmChargerBlock;
    protected Item zpmChargerItem;
    protected AnalyzerAncientItem analyzerAncientItem;
    protected RingsBlock ringsBlock;
    protected RingsControllerBlock ringsControllerBlock;
    protected InvisibleBlock invisibleBlock;
    protected Item ringsItem;
    protected Item ringsControllerItem;
    protected Item invisibleItem;
    protected SimpleNetworkWrapper networkWrapper;

    public void preInit(FMLPreInitializationEvent event) throws Exception {
        raumshipsItemTab = new RaumshipsItemTab();

        registerModEntity(PuddleJumperEntity.ID, PuddleJumperEntity.NAME, PuddleJumperEntity.class, Color.BLACK, Color.WHITE);
        registerModEntity(DeathGliderEntity.ID, DeathGliderEntity.NAME, DeathGliderEntity.class, Color.BLUE, Color.WHITE);
        registerModEntity(AlKeshEntity.ID, AlKeshEntity.NAME, AlKeshEntity.class, Color.YELLOW, Color.WHITE);
        registerModEntity(F301Entity.ID, F301Entity.NAME, F301Entity.class, Color.GREEN, Color.WHITE);
        registerModEntity(F302Entity.ID, F302Entity.NAME, F302Entity.class, Color.GREEN, Color.YELLOW);
        registerModEntity(GateGliderEntity.ID, GateGliderEntity.NAME, GateGliderEntity.class, Color.GREEN, Color.RED);
        registerModEntity(TeleakEntity.ID, TeleakEntity.NAME, TeleakEntity.class, Color.YELLOW, Color.RED);
        registerModEntity(X304Entity.ID, X304Entity.NAME, X304Entity.class, Color.WHITE, Color.BLACK);
        registerModEntity(HatakEntity.ID, HatakEntity.NAME, HatakEntity.class, Color.BLACK, Color.YELLOW);
        registerModEntity(AtlantisEntity.ID, AtlantisEntity.NAME, AtlantisEntity.class, Color.BLUE, Color.BLUE);

        zpmHubBlock = new ZPMHubBlock();
        zpmHubItem = registerModBlock(zpmHubBlock);

        zpmChargerBlock = new ZPMChargerBlock();
        zpmChargerItem = registerModBlock(zpmChargerBlock);

        zpmItem = new ZPMItem();
        registerModItem(zpmItem);

        analyzerAncientItem = new AnalyzerAncientItem();
        registerModItem(analyzerAncientItem);

        ringsBlock = new RingsBlock();
        ringsItem = registerModBlock(ringsBlock);

        ringsControllerBlock = new RingsControllerBlock();
        ringsControllerItem = registerModBlock(ringsControllerBlock);

        invisibleBlock = new InvisibleBlock();
        invisibleItem = registerModBlock(invisibleBlock);

        GameRegistry.registerTileEntity(ZPMHubTileEntity.class, new ResourceLocation(MODID, "zpm_hub_tile_entity"));
        GameRegistry.registerTileEntity(ZPMChargerTileEntity.class, new ResourceLocation(MODID, "zpm_charger_tile_entity"));
        GameRegistry.registerTileEntity(RingsTile.class, new ResourceLocation(MODID, "rings_tile_entity"));
        GameRegistry.registerTileEntity(RingsControllerTile.class, new ResourceLocation(MODID, "rings_controller_tile_entity"));

        NetworkRegistry.INSTANCE.registerGuiHandler(RaumShipsMod.instance, new RaumshipsGuiHandler());

        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        int id = 0;

        networkWrapper.registerMessage(StateUpdateServerHandler.class, StateUpdateRequestToServer.class, id, Side.SERVER); id++;
        networkWrapper.registerMessage(SaveRingsParametersServerHandler.class, SaveRingsParametersToServer.class, id, Side.SERVER); id++;
        networkWrapper.registerMessage(RingsControllerActivatedToServer.RingsControllerActivatedServerHandler.class, RingsControllerActivatedToServer.class, id, Side.SERVER); id++;
        networkWrapper.registerMessage(TileUpdateServerHandler.class, RendererUpdateRequestToServer.class, id, Side.CLIENT); id++;

        networkWrapper.registerMessage(TileUpdateClientHandler.class, RendererUpdatePacketToClient.class, id, Side.CLIENT); id++;
        networkWrapper.registerMessage(StartRingsAnimationToClientHandler.class, StartRingsAnimationToClient.class, id, Side.CLIENT); id++;
        networkWrapper.registerMessage(StartPlayerFadeOutToClientHandler.class, StartPlayerFadeOutToClient.class, id, Side.CLIENT); id++;
        networkWrapper.registerMessage(StateUpdateClientHandler.class, StateUpdatePacketToClient.class, id, Side.CLIENT); id++;
        networkWrapper.registerMessage(RingsControllerActivatedToClientHandler.class, RingsControllerActivatedToClient.class, id, Side.CLIENT); id++;
    }

    public void load(FMLInitializationEvent event) throws Exception {}

    public void postInit(FMLPostInitializationEvent event) {}

    private Item registerModBlock(Block block) {
        ForgeRegistries.BLOCKS.register(block);
        block.setCreativeTab(raumshipsItemTab);

        Item item = new ItemBlock(block).setRegistryName(block.getRegistryName());
        registerModItem(item);
        return item;
    }

    private void registerModItem(Item item) {
        ForgeRegistries.ITEMS.register(item);
        item.setCreativeTab(raumshipsItemTab);
    }

    private void registerModEntity(int id, String name, Class<? extends RaumShipsEntity> entityClass, Color c1, Color c2) {
        EntityRegistry.registerModEntity(
                new ResourceLocation(MODID, name),
                entityClass,
                name,
                id,
                RaumShipsMod.instance,
                RSCommonConstants.RENDER_DISTANCE,
                1,
                true,
                c1.getRGB(),
                c2.getRGB());
    }

    public abstract EntityPlayer getPlayerClientSide();
    public abstract void addScheduledTaskClientSide(Runnable runnable);

    public InvisibleBlock getInvisibleBlock() {
        return invisibleBlock;
    }

    public SimpleNetworkWrapper getNetworkWrapper() {
        return networkWrapper;
    }
}
