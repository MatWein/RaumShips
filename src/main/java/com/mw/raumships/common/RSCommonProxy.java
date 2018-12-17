package com.mw.raumships.common;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.common.blocks.ZPMHubBlock;
import com.mw.raumships.common.blocks.ZPMHubTileEntity;
import com.mw.raumships.common.entities.AlKeshEntity;
import com.mw.raumships.common.entities.DeathGliderEntity;
import com.mw.raumships.common.entities.PuddleJumperEntity;
import com.mw.raumships.common.entities.RaumShipsEntity;
import com.mw.raumships.common.gui.RaumshipsGuiHandler;
import com.mw.raumships.common.items.ZPMItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.awt.*;

import static com.mw.raumships.RaumShipsMod.MODID;

public class RSCommonProxy {
    public static final int RENDER_DISTANCE = 100;

    protected RaumshipsItemTab raumshipsItemTab;
    protected ZPMHubBlock zpmHubBlock;
    protected Item zpmHubItem;
    protected ZPMItem zpmItem;

    public void preInit(FMLPreInitializationEvent event) throws Exception {
        raumshipsItemTab = new RaumshipsItemTab();

        registerModEntity(PuddleJumperEntity.ID, PuddleJumperEntity.NAME, PuddleJumperEntity.class, Color.BLACK, Color.WHITE);
        registerModEntity(DeathGliderEntity.ID, DeathGliderEntity.NAME, DeathGliderEntity.class, Color.BLUE, Color.WHITE);
        registerModEntity(AlKeshEntity.ID, AlKeshEntity.NAME, AlKeshEntity.class, Color.YELLOW, Color.WHITE);

        zpmHubBlock = new ZPMHubBlock();
        zpmHubItem = registerModBlock(zpmHubBlock);

        zpmItem = new ZPMItem();
        registerModItem(zpmItem);

        GameRegistry.registerTileEntity(ZPMHubTileEntity.class, new ResourceLocation(MODID, "zpm_hub_tile_entity"));

        NetworkRegistry.INSTANCE.registerGuiHandler(RaumShipsMod.instance, new RaumshipsGuiHandler());
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
                RENDER_DISTANCE,
                1,
                true,
                c1.getRGB(),
                c2.getRGB());
    }
}
