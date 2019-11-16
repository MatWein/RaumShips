package com.mw.raumships.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class Keybinds {
    private static final String RS_KEYS = "raumships.keys";

    public static KeyBinding rsForward;
    public static KeyBinding rsBack;
    public static KeyBinding rsLeft;
    public static KeyBinding rsRight;
    public static KeyBinding rsUp;
    public static KeyBinding rsDown;
    public static KeyBinding rsCtrl;

    public static void init() {
        rsForward = new KeyBinding("rs.key.moveForward", 87, RS_KEYS);
        rsBack = new KeyBinding("rs.key.moveBack", 83, RS_KEYS);
        rsLeft = new KeyBinding("rs.key.moveLeft", 65, RS_KEYS);
        rsRight = new KeyBinding("rs.key.moveRight", 68, RS_KEYS);
        rsUp = new KeyBinding("rs.key.moveUp", 32, RS_KEYS);
        rsDown = new KeyBinding("rs.key.moveDown", 88, RS_KEYS);
        rsCtrl = new KeyBinding("rs.key.ctrl", 341, RS_KEYS);

        ClientRegistry.registerKeyBinding(rsForward);
        ClientRegistry.registerKeyBinding(rsBack);
        ClientRegistry.registerKeyBinding(rsLeft);
        ClientRegistry.registerKeyBinding(rsRight);
        ClientRegistry.registerKeyBinding(rsUp);
        ClientRegistry.registerKeyBinding(rsDown);
        ClientRegistry.registerKeyBinding(rsCtrl);
    }
}
