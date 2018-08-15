package com.mw.raumships.common;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.common.entities.PuddleJumperEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.awt.*;

import static com.mw.raumships.RaumShipsMod.MODID;

public class RSCommonProxy {
    public void preInit(FMLPreInitializationEvent event) throws Exception {
        EntityRegistry.registerModEntity(
                new ResourceLocation(MODID, PuddleJumperEntity.NAME),
                PuddleJumperEntity.class,
                PuddleJumperEntity.NAME,
                PuddleJumperEntity.ID,
                RaumShipsMod.instance,
                PuddleJumperEntity.RENDER_DISTANCE,
                1,
                true,
                Color.BLACK.getRGB(),
                Color.WHITE.getRGB());
    }

    public void load(FMLInitializationEvent event) throws Exception {}

    public void postInit(FMLPostInitializationEvent event) {}
}
