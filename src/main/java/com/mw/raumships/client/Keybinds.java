package com.mw.raumships.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class Keybinds {
    private static final String RS_KEYS = "raumships.keys";

    public static KeyBinding rsForward;
    public static KeyBinding rsBack;
    public static KeyBinding rsLeft;
    public static KeyBinding rsRight;
    public static KeyBinding rsUp;
    public static KeyBinding rsDown;
    public static KeyBinding rsCtrl;

    public static KeyBinding number1Ctrl;
    public static KeyBinding number2Ctrl;
    public static KeyBinding number3Ctrl;
    public static KeyBinding number4Ctrl;
    public static KeyBinding number5Ctrl;
    public static KeyBinding number6Ctrl;

    public static void init() {
        // Ships
        rsForward = registerKeyBinding("rs.key.moveForward", Keyboard.KEY_W);
        rsBack = registerKeyBinding("rs.key.moveBack", Keyboard.KEY_S);
        rsLeft = registerKeyBinding("rs.key.moveLeft", Keyboard.KEY_A);
        rsRight = registerKeyBinding("rs.key.moveRight", Keyboard.KEY_D);
        rsUp = registerKeyBinding("rs.key.moveUp", Keyboard.KEY_SPACE);
        rsDown = registerKeyBinding("rs.key.moveDown", Keyboard.KEY_X);
        rsCtrl = registerKeyBinding("rs.key.ctrl", Keyboard.KEY_LCONTROL);

        // Transport rings
        number1Ctrl = registerKeyBinding("rs.key.number1Ctrl", Keyboard.KEY_NUMPAD1);
        number2Ctrl = registerKeyBinding("rs.key.number2Ctrl", Keyboard.KEY_NUMPAD2);
        number3Ctrl = registerKeyBinding("rs.key.number3Ctrl", Keyboard.KEY_NUMPAD3);
        number4Ctrl = registerKeyBinding("rs.key.number4Ctrl", Keyboard.KEY_NUMPAD4);
        number5Ctrl = registerKeyBinding("rs.key.number5Ctrl", Keyboard.KEY_NUMPAD5);
        number6Ctrl = registerKeyBinding("rs.key.number6Ctrl", Keyboard.KEY_NUMPAD6);
    }

    private static KeyBinding registerKeyBinding(String translationKey, int key) {
        KeyBinding keyBinding = new KeyBinding(translationKey, key, RS_KEYS);
        ClientRegistry.registerKeyBinding(keyBinding);
        return keyBinding;
    }
}
