package com.mw.raumships.common;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.common.entities.PuddleJumperEntity;
import com.mw.raumships.common.entities.PuddleJumperEntityConstants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.awt.*;

public class RSCommonProxy {
    public void preInit(FMLPreInitializationEvent event) throws Exception {
        EntityRegistry.registerModEntity(
                new ResourceLocation(RaumShipsMod.MODID, PuddleJumperEntityConstants.NAME),
                PuddleJumperEntity.class,
                PuddleJumperEntityConstants.NAME,
                PuddleJumperEntityConstants.ID,
                RaumShipsMod.instance,
                PuddleJumperEntityConstants.RENDER_DISTANCE,
                1,
                true,
                Color.BLACK.getRGB(),
                Color.WHITE.getRGB());
    }

    public void load(FMLInitializationEvent event) throws Exception {}

    public void postInit(FMLPostInitializationEvent event) {}
}
