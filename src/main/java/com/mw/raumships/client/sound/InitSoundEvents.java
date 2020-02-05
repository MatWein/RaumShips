package com.mw.raumships.client.sound;

import com.mw.raumships.RaumShipsMod;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(value = Side.CLIENT, modid = RaumShipsMod.MODID)
public class InitSoundEvents {
    @SubscribeEvent
    public static void registerSoundEvents(Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                Sounds.JUMPER_ENGINE,
                Sounds.ALKESH_ENGINE,
                Sounds.TELEAK_ENGINE,
                Sounds.HATAK_ENGINE,
                Sounds.X304_ENGINE,
                Sounds.DEATH_GLIDER_ENGINE,
                Sounds.RINGS_TRANSPORT,
                Sounds.RINGS_CONTROLLER_BUTTON
        );
    }
}
