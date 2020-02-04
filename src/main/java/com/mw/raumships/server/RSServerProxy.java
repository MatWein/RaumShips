package com.mw.raumships.server;

import com.mw.raumships.common.RSCommonProxy;
import net.minecraft.entity.player.EntityPlayer;

public class RSServerProxy extends RSCommonProxy {
    @Override
    public EntityPlayer getPlayerClientSide() {
        return null;
    }

    @Override
    public void addScheduledTaskClientSide(Runnable runnable) {

    }
}
