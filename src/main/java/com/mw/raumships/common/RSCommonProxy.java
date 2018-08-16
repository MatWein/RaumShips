package com.mw.raumships.common;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.common.entities.AlKeshEntity;
import com.mw.raumships.common.entities.DeathGliderEntity;
import com.mw.raumships.common.entities.PuddleJumperEntity;
import com.mw.raumships.common.entities.RaumShipsEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.awt.*;

import static com.mw.raumships.RaumShipsMod.MODID;

public class RSCommonProxy {
    public static final int RENDER_DISTANCE = 100;

    public void preInit(FMLPreInitializationEvent event) throws Exception {
        registerModEntity(PuddleJumperEntity.ID, PuddleJumperEntity.NAME, PuddleJumperEntity.class, Color.BLACK, Color.WHITE);
        registerModEntity(DeathGliderEntity.ID, DeathGliderEntity.NAME, DeathGliderEntity.class, Color.BLUE, Color.WHITE);
        registerModEntity(AlKeshEntity.ID, AlKeshEntity.NAME, AlKeshEntity.class, Color.YELLOW, Color.WHITE);
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

    public void load(FMLInitializationEvent event) throws Exception {}

    public void postInit(FMLPostInitializationEvent event) {}
}
