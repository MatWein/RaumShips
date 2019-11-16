package com.mw.raumships.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;

public class ClientUtils {
    public static Minecraft getMc() {
        return Minecraft.getInstance();
    }

    public static SoundHandler getSh() {
        return getMc().getSoundHandler();
    }

    public static boolean isThirdPersonView() {
        return getMc().gameSettings.thirdPersonView != 0;
    }

    public static boolean isEgoPersonView() {
        return !isThirdPersonView();
    }
}
