package com.mw.raumships.client;

import com.mw.raumships.RaumShipsMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class Sounds {
    public static final SoundEvent JUMPER_ENGINE = createSoundEvent("sound.engine");

    private static SoundEvent createSoundEvent(String soundName) {
        final ResourceLocation soundID = new ResourceLocation(RaumShipsMod.MODID, soundName);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }
}
