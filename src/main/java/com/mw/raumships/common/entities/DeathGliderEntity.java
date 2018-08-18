package com.mw.raumships.common.entities;

import com.mw.raumships.client.sound.Sounds;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import static com.mw.raumships.RaumShipsMod.MODID;

public class DeathGliderEntity extends RaumShipsEntity {
    public static final int ID = 1002;
    public static final String NAME = "DeathGlider";

    public static final ResourceLocation MODEL = new ResourceLocation(MODID, "models/entity/deathglider.obj");
    public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "models/entity/deathglider.png");
    public static final float SOUND_VOLUME = 0.6F;

    public static final float THIRD_PERSON_DISTANCE = 8.5F;
    public static final float COCKPIT_CAMERA_Z_OFFSET = -1.7F;
    public static final float SCALING_FACTOR = 0.6F;
    public static final float[] PLAYER_MOUNT_POSITION_X_OFFSET = new float[] { 0.0F };
    public static final float PLAYER_MOUNT_POSITION_Y_OFFSET = 0.8F;
    public static final float[] PLAYER_MOUNT_POSITION_Z_OFFSET = new float[] { 0.0F };
    public static final float RENDER_Y_OFFSET = 0.5F;

    public static final float BOUNDING_BOX_WIDTH = 4.0F;
    public static final float BOUNDING_BOX_HEIGHT = 3.0F;
    public static final int MAX_PASSANGERS = 1;

    public static final float FINAL_AIRSHIP_SPEED_TURN = 0.72F;
    public static final float FINAL_AIRSHIP_SPEED_FORWARD = 0.32F;
    public static final float FINAL_AIRSHIP_SPEED_UP = 0.032F;
    public static final float FINAL_AIRSHIP_SPEED_DOWN = 0.016F;
    public static final float SPEED_MODIFIER = 0.16F;
    public static final float MOMENTUM = 0.7F;

    public DeathGliderEntity(World worldIn) {
        super(worldIn);

        setSize(BOUNDING_BOX_WIDTH, BOUNDING_BOX_HEIGHT);
    }

    @Override
    public ResourceLocation getModelResourceLocation() {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResourceLocation() {
        return TEXTURE;
    }

    @Override
    public SoundEvent getSound() {
        return Sounds.DEATH_GLIDER_ENGINE;
    }

    @Override
    public float getVolume() {
        return SOUND_VOLUME;
    }

    @Override
    public float getRenderCockpitCameraZOffset() {
        return COCKPIT_CAMERA_Z_OFFSET;
    }

    @Override
    public float getRenderScalingFactor() {
        return SCALING_FACTOR;
    }

    @Override
    public float getThirdPersonDistance() {
        return THIRD_PERSON_DISTANCE;
    }

    @Override
    public int getMaxPassangers() {
        return MAX_PASSANGERS;
    }

    @Override
    public float[] getPlayerMountPositionXOffset() {
        return PLAYER_MOUNT_POSITION_X_OFFSET;
    }

    @Override
    public float getPlayerMountPositionYOffset() {
        return PLAYER_MOUNT_POSITION_Y_OFFSET;
    }

    @Override
    public float[] getPlayerMountPositionZOffset() {
        return PLAYER_MOUNT_POSITION_Z_OFFSET;
    }

    @Override
    public float getRenderYOffset() {
        return RENDER_Y_OFFSET;
    }

    @Override
    public float getFinalAirShipSpeedTurn() {
        return FINAL_AIRSHIP_SPEED_TURN;
    }

    @Override
    public float getFinalAirShipSpeedForward() {
        return FINAL_AIRSHIP_SPEED_FORWARD;
    }

    @Override
    public float getFinalAirShipSpeedUp() {
        return FINAL_AIRSHIP_SPEED_UP;
    }

    @Override
    public float getFinalAirShipSpeedDown() {
        return FINAL_AIRSHIP_SPEED_DOWN;
    }

    @Override
    public float getSpeedModifier() {
        return SPEED_MODIFIER;
    }

    @Override
    public float getMomentum() {
        return MOMENTUM;
    }
}
