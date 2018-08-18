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
    public static KeyBinding rsCrtl;

    public static void init() {
        rsForward = new KeyBinding("rs.key.moveForward", Keyboard.KEY_W, RS_KEYS);
        rsBack = new KeyBinding("rs.key.moveBack", Keyboard.KEY_S, RS_KEYS);
        rsLeft = new KeyBinding("rs.key.moveLeft", Keyboard.KEY_A, RS_KEYS);
        rsRight = new KeyBinding("rs.key.moveRight", Keyboard.KEY_D, RS_KEYS);
        rsUp = new KeyBinding("rs.key.moveUp", Keyboard.KEY_SPACE, RS_KEYS);
        rsDown = new KeyBinding("rs.key.moveDown", Keyboard.KEY_X, RS_KEYS);
        rsCrtl = new KeyBinding("rs.key.crtl", Keyboard.KEY_LCONTROL, RS_KEYS);

        ClientRegistry.registerKeyBinding(rsForward);
        ClientRegistry.registerKeyBinding(rsBack);
        ClientRegistry.registerKeyBinding(rsLeft);
        ClientRegistry.registerKeyBinding(rsRight);
        ClientRegistry.registerKeyBinding(rsUp);
        ClientRegistry.registerKeyBinding(rsDown);
        ClientRegistry.registerKeyBinding(rsCrtl);
    }
}
