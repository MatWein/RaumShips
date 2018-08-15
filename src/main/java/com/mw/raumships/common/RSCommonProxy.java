package com.mw.raumships.common;

import com.mw.raumships.RaumShipsMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.awt.*;

public class RSCommonProxy {
    public void preInit(FMLPreInitializationEvent event) throws Exception {
        EntityRegistry.registerModEntity(
                new ResourceLocation(RaumShipsMod.MODID, "PuddleJumper"),
                PuddleJumperEntity.class,
                "PuddleJumper",
                1001,
                RaumShipsMod.instance,
                100,
                1,
                true,
                new Color(0, 0, 0).getRGB(),
                new Color(255, 255, 255).getRGB());
    }

    public void load(FMLInitializationEvent event) throws Exception {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}
