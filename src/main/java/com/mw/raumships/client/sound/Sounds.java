package com.mw.raumships.client.sound;

import com.mw.raumships.RaumShipsMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class Sounds {
    public static final SoundEvent JUMPER_ENGINE = createSoundEvent("sound.engine");
    public static final SoundEvent ALKESH_ENGINE = createSoundEvent("sound.engine2");
    public static final SoundEvent DEATH_GLIDER_ENGINE = createSoundEvent("sound.engine3");

    private static SoundEvent createSoundEvent(String soundName) {
        final ResourceLocation soundID = new ResourceLocation(RaumShipsMod.MODID, soundName);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }
}
