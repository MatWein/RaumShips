package com.mw.raumships.client.sound;

import com.mw.raumships.common.entities.RaumShipsEntity;
import net.minecraft.client.audio.Sound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class FlyingEntitySoundLoop extends Sound {
    private RaumShipsEntity entity;
    private final float volumeToUse;

    public FlyingEntitySoundLoop(RaumShipsEntity entity, SoundEvent sound, float volume) {
        super(sound, SoundCategory.AMBIENT);

        this.entity = entity;
        this.repeat = true;
        this.volume = volume;
        this.volumeToUse = volume;
    }

    @Override
    public void update() {
        boolean shouldPlay = entity.canPassengerSteer() || entity.isInAir();

        if (entity.isDead) {
            this.donePlaying = true;
        } else {
            this.xPosF = (float)entity.posX;
            this.yPosF = (float)entity.posY;
            this.zPosF = (float)entity.posZ;

            this.volume = shouldPlay ? volumeToUse : 0.0F;
        }
    }
}
