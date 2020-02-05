package com.mw.raumships.client.gui.rings;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ITileEntityStateProvider {
    State getState(EnumStateType stateType);

    State createState(EnumStateType stateType);

    @SideOnly(Side.CLIENT)
    void setState(EnumStateType stateType, State state);
}
