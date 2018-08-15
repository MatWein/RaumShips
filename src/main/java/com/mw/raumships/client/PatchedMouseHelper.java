package com.mw.raumships.client;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.common.entities.PuddleJumperEntity;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MouseHelper;

public class PatchedMouseHelper extends MouseHelper {
    @Override
    public void mouseXYChange() {
        EntityPlayerSP player = RaumShipsMod.mc.player;

        if (player != null && player.getRidingEntity() instanceof PuddleJumperEntity && RaumShipsMod.mc.gameSettings.thirdPersonView == 0) {
            this.deltaX = 0;
            this.deltaY = 0;
            return;
        }

        super.mouseXYChange();
    }
}
