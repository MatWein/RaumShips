package com.mw.raumships.client.sound;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.common.entities.RaumShipsEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import static com.mw.raumships.client.ClientUtils.getSh;

public class Sounds {
    public static final SoundEvent JUMPER_ENGINE = createSoundEvent("sound.engine");
    public static final SoundEvent ALKESH_ENGINE = createSoundEvent("sound.engine2");
    public static final SoundEvent TELEAK_ENGINE = createSoundEvent("sound.teleak");
    public static final SoundEvent HATAK_ENGINE = createSoundEvent("sound.hatak");
    public static final SoundEvent X304_ENGINE = createSoundEvent("sound.x304");
    public static final SoundEvent DEATH_GLIDER_ENGINE = createSoundEvent("sound.engine3");

    private static SoundEvent createSoundEvent(String soundName) {
        final ResourceLocation soundID = new ResourceLocation(RaumShipsMod.MODID, soundName);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }

    public static void playSound(RaumShipsEntity entity, SoundEvent soundEvent, float volume) {
        FlyingEntitySoundLoop sound = new FlyingEntitySoundLoop(entity, soundEvent, volume);
        getSh().playSound(sound);
    }
}
