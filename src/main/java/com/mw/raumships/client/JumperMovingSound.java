package com.mw.raumships.client;

import com.mw.raumships.common.PuddleJumperEntity;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;

public class JumperMovingSound extends MovingSound {
    private PuddleJumperEntity puddleJumperEntity;

    public JumperMovingSound(PuddleJumperEntity puddleJumperEntity) {
        super(Sounds.JUMPER_ENGINE, SoundCategory.AMBIENT);

        this.puddleJumperEntity = puddleJumperEntity;
        this.repeat = true;
        this.volume = 0.3F;
    }

    @Override
    public void update() {
        boolean shouldPlay = (puddleJumperEntity.getControllingPassenger() instanceof EntityPlayer || puddleJumperEntity.isInAir());

        if (puddleJumperEntity.isDead) {
            this.donePlaying = true;
        } else {
            this.xPosF = (float)puddleJumperEntity.posX;
            this.yPosF = (float)puddleJumperEntity.posY;
            this.zPosF = (float)puddleJumperEntity.posZ;

            this.volume = shouldPlay ? 0.3F : 0.0F;
        }
    }
}
