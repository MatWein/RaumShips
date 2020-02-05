package com.mw.raumships.client.gui.rings;

public enum EnumStateType {
    GUI_STATE(0);

    public int id;

    EnumStateType(int id) {
        this.id = id;
    }

    public static EnumStateType byId(int id) {
        return EnumStateType.values()[id];
    }
}
