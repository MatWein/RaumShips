package com.mw.raumships.client;

import com.mw.raumships.common.PuddleJumperEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MouseHelper;

public class PatchedMouseHelper extends MouseHelper {
    @Override
    public void mouseXYChange() {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.player;

        if (player != null && player.getRidingEntity() instanceof PuddleJumperEntity && minecraft.gameSettings.thirdPersonView == 0) {
            this.deltaX = 0;
            this.deltaY = 0;
            return;
        }

        super.mouseXYChange();
    }
}
