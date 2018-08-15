package net.minecraft.client.renderer;

import com.mw.raumships.common.entities.PuddleJumperEntity;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MouseHelper;

import static com.mw.raumships.RaumShipsMod.getMc;

public class PatchedMouseHelper extends MouseHelper {
    @Override
    public void mouseXYChange() {
        EntityPlayerSP player = getMc().player;

        if (player != null && player.getRidingEntity() instanceof PuddleJumperEntity && getMc().gameSettings.thirdPersonView == 0) {
            this.deltaX = 0;
            this.deltaY = 0;
            return;
        }

        super.mouseXYChange();
    }
}
