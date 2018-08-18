package com.mw.raumships.client.sound;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.common.entities.RaumShipsEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

import static com.mw.raumships.client.ClientUtils.getSh;

public class Sounds {
    private static final Map<Entity, FlyingEntitySoundLoop> cachedSounds = new HashMap<>();

    public static final SoundEvent JUMPER_ENGINE = createSoundEvent("sound.engine");
    public static final SoundEvent ALKESH_ENGINE = createSoundEvent("sound.engine2");
    public static final SoundEvent DEATH_GLIDER_ENGINE = createSoundEvent("sound.engine3");

    private static SoundEvent createSoundEvent(String soundName) {
        final ResourceLocation soundID = new ResourceLocation(RaumShipsMod.MODID, soundName);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }

    @SideOnly(Side.CLIENT)
    public static void playSound(RaumShipsEntity entity, SoundEvent soundEvent, float volume) {
        FlyingEntitySoundLoop sound = getOrCreateSound(entity, soundEvent, volume);
        if (!getSh().isSoundPlaying(sound)) {
            getSh().playSound(sound);
        }
    }

    private static FlyingEntitySoundLoop getOrCreateSound(RaumShipsEntity entity, SoundEvent soundEvent, float volume) {
        FlyingEntitySoundLoop existingSound = cachedSounds.get(entity);
        if (existingSound != null) {
            return existingSound;
        }

        FlyingEntitySoundLoop newSound = new FlyingEntitySoundLoop(entity, soundEvent, volume);
        cachedSounds.put(entity, newSound);
        return newSound;
    }
}
