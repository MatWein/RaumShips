package com.mw.raumships.common.entities;

import com.mw.raumships.client.sound.Sounds;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import static com.mw.raumships.RaumShipsMod.MODID;

public class PuddleJumperEntity extends RaumShipsEntity {
    public static final int ID = 1001;
    public static final String NAME = "PuddleJumper";

    public static final ResourceLocation MODEL = new ResourceLocation(MODID, "models/entity/puddlejumper.obj");
    public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "models/entity/puddlejumper.png");
    public static final float SOUND_VOLUME = 0.3F;

    public static final float THIRD_PERSON_DISTANCE = 8.5F;
    public static final float COCKPIT_CAMERA_Z_OFFSET = -1.5F;
    public static final float SCALING_FACTOR = 0.4F;
    public static final float PLAYER_MOUNT_POSITION_X_OFFSET = 2.0F;
    public static final float PLAYER_MOUNT_POSITION_Y_OFFSET = 0.0F;
    public static final float RENDER_Y_OFFSET = 0.0F;

    public static final float BOUNDING_BOX_WIDTH = 2.5F;
    public static final float BOUNDING_BOX_HEIGHT = 2.5F;
    public static final int MAX_PASSANGERS = 2;

    public static final float FINAL_AIRSHIP_SPEED_TURN = 0.18F;
    public static final float FINAL_AIRSHIP_SPEED_FORWARD = 0.04F;
    public static final float FINAL_AIRSHIP_SPEED_UP = 0.004F;
    public static final float FINAL_AIRSHIP_SPEED_DOWN = 0.004F;
    public static final float SPEED_MODIFIER = 0.02F;
    public static final float MOMENTUM = 0.9F;

    public PuddleJumperEntity(World worldIn) {
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
        return Sounds.JUMPER_ENGINE;
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
    public float getPlayerMountPositionXOffset() {
        return PLAYER_MOUNT_POSITION_X_OFFSET;
    }

    @Override
    public float getPlayerMountPositionYOffset() {
        return PLAYER_MOUNT_POSITION_Y_OFFSET;
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
