package com.mw.raumships.client.sound;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.common.entities.RaumShipsEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.mw.raumships.client.ClientUtils.getSh;

public class Sounds {
    public static final SoundEvent JUMPER_ENGINE = createSoundEvent("sound.engine");
    public static final SoundEvent ALKESH_ENGINE = createSoundEvent("sound.engine2");
    public static final SoundEvent TELEAK_ENGINE = createSoundEvent("sound.teleak");
    public static final SoundEvent HATAK_ENGINE = createSoundEvent("sound.hatak");
    public static final SoundEvent X304_ENGINE = createSoundEvent("sound.x304");
    public static final SoundEvent DEATH_GLIDER_ENGINE = createSoundEvent("sound.engine3");
    public static final SoundEvent RINGS_TRANSPORT = createSoundEvent("sound.rings_transport");
    public static final SoundEvent RINGS_CONTROLLER_BUTTON = createSoundEvent("sound.rings_controller_button");

    private static SoundEvent createSoundEvent(String soundName) {
        final ResourceLocation soundID = new ResourceLocation(RaumShipsMod.MODID, soundName);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }

    public static void playSoundEvent(World world, BlockPos pos, SoundEvent soundEvent, float volume) {
        world.playSound(null, pos, soundEvent, SoundCategory.AMBIENT, volume, 1.0f);
    }

    public static void playSound(RaumShipsEntity entity, SoundEvent soundEvent, float volume) {
        FlyingEntitySoundLoop sound = new FlyingEntitySoundLoop(entity, soundEvent, volume);
        getSh().playSound(sound);
    }
}
