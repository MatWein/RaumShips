package net.minecraft.client.renderer;

import com.mw.raumships.common.entities.RaumShipsEntity;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MouseHelper;

import static com.mw.raumships.client.ClientUtils.getMc;
import static com.mw.raumships.client.ClientUtils.isEgoPersonView;

public class PatchedMouseHelper extends MouseHelper {
    @Override
    public void mouseXYChange() {
        EntityPlayerSP player = getMc().player;

        if (player != null && player.getRidingEntity() instanceof RaumShipsEntity && isEgoPersonView()) {
            this.deltaX = 0;
            this.deltaY = 0;
            return;
        }

        super.mouseXYChange();
    }
}
