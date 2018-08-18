package com.mw.raumships.common.entities;

import com.mw.raumships.client.Keybinds;
import com.mw.raumships.client.sound.FlyingEntitySoundLoop;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.PatchedEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static com.mw.raumships.client.ClientUtils.getMc;
import static com.mw.raumships.client.ClientUtils.getSh;
import static com.mw.raumships.common.RSCommonConstants.*;

public abstract class RaumShipsEntity extends EntityLiving  {
    protected final FlyingEntitySoundLoop sound;
    protected final OBJModel model;

    protected float deltaRotation;

    protected boolean leftInputDown;
    protected boolean rightInputDown;
    protected boolean forwardInputDown;
    protected boolean backInputDown;
    protected boolean upInputDown;
    protected boolean downInputDown;

    protected int lerpSteps;
    protected double lerpPitch;
    protected double lerpX;
    protected double lerpY;
    protected double lerpZ;
    protected double lerpYaw;

    public RaumShipsEntity(World worldIn) {
        super(worldIn);

        this.ignoreFrustumCheck = true;
        this.preventEntitySpawning = true;
        this.sound = new FlyingEntitySoundLoop(this, getSound(), getVolume());
        try {
            this.model = (OBJModel) OBJLoader.INSTANCE.loadModel(getModelResourceLocation());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public abstract ResourceLocation getModelResourceLocation();
    public abstract ResourceLocation getTextureResourceLocation();
    public abstract SoundEvent getSound();
    public abstract float getVolume();

    public abstract float getRenderCockpitCameraZOffset();
    public abstract float getRenderScalingFactor();
    public abstract float getThirdPersonDistance();
    public abstract int getMaxPassangers();
    public abstract float[] getPlayerMountPositionXOffset();
    public abstract float getPlayerMountPositionYOffset();
    public abstract float[] getPlayerMountPositionZOffset();
    public abstract float getRenderYOffset();

    public abstract float getFinalAirShipSpeedTurn();
    public abstract float getFinalAirShipSpeedForward();
    public abstract float getFinalAirShipSpeedUp();
    public abstract float getFinalAirShipSpeedDown();
    public abstract float getSpeedModifier();
    public abstract float getMomentum();

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        super.onEntityUpdate();

        this.tickLerp();
        this.playSoundLoop();

        if (this.canPassengerSteer()) {
            this.updateMotion();

            if (this.world.isRemote) {
                this.controlAirship();
                this.updateInputs();
                this.updateThirdPersonDistance(getThirdPersonDistance());
                this.updatePlayerCamera();
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        } else {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
        }

        this.doBlockCollisions();

        EntityPlayerSP player = getMc().player;
        if (player != null && player.isSneaking()) {
            this.updateThirdPersonDistance(DEFAULT_MINECRAFT_VIEW_DISTANCE);
            getMc().gameSettings.hideGUI = false;
            player.dismountRidingEntity();
        }
    }

    protected void tickLerp() {
        if (this.lerpSteps > 0 && !this.canPassengerSteer()) {
            double d0 = this.posX + (this.lerpX - this.posX) / (double) this.lerpSteps;
            double d1 = this.posY + (this.lerpY - this.posY) / (double) this.lerpSteps;
            double d2 = this.posZ + (this.lerpZ - this.posZ) / (double) this.lerpSteps;
            double d3 = MathHelper.wrapDegrees(this.lerpYaw - (double) this.rotationYaw);
            this.rotationYaw = (float) ((double) this.rotationYaw + d3 / (double) this.lerpSteps);
            this.rotationPitch = (float) ((double) this.rotationPitch + (this.lerpPitch - (double) this.rotationPitch) / (double) this.lerpSteps);
            --this.lerpSteps;
            this.setPosition(d0, d1, d2);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
    }

    private void updateMotion() {
        this.motionX *= getMomentum();
        this.motionY *= getMomentum();
        this.motionZ *= getMomentum();
        this.deltaRotation *= getMomentum();
    }

    public void controlAirship() {
        if (this.isBeingRidden()) {
            float f = 0.0F;
            float f1 = 0.0F;

            if (this.leftInputDown) {
                this.deltaRotation -= (getFinalAirShipSpeedTurn() + (getSpeedModifier() * 2));
            }

            if (this.rightInputDown) {
                this.deltaRotation += (getFinalAirShipSpeedTurn() + (getSpeedModifier() * 2));
            }

            if (this.rightInputDown != this.leftInputDown && !this.forwardInputDown && !this.backInputDown) {
                f += 0.005F;
            }

            this.rotationYaw += this.deltaRotation;

            if (this.forwardInputDown) {
                f += getFinalAirShipSpeedForward();
            }

            if (this.backInputDown) {
                f -= 0.005F;
            }

            if (this.upInputDown) {
                f1 += getFinalAirShipSpeedUp() + (getSpeedModifier() / 32);
            }

            if (this.downInputDown) {
                f1 -= getFinalAirShipSpeedDown() + (getSpeedModifier() / 32);
            }

            this.motionX += (double) (MathHelper.sin(-this.rotationYaw * ROTATION_FACTOR) * f);
            this.motionZ += (double) (MathHelper.cos(this.rotationYaw * ROTATION_FACTOR) * f);
            this.motionY += (double) (Y_FACTOR * f1);

            this.rotationPitch += 10;
        }
    }

    @SideOnly(Side.CLIENT)
    public void updatePlayerCamera() {
        EntityPlayerSP player = getMc().player;
        if (player != null && getMc().gameSettings.thirdPersonView == 0) {
            player.rotationYaw = this.rotationYaw;
            player.rotationPitch = 0.0F;
            getMc().gameSettings.hideGUI = true;
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateThirdPersonDistance(float distance) {
        EntityRenderer renderer = getMc().entityRenderer;
        if (renderer instanceof PatchedEntityRenderer) {
            ((PatchedEntityRenderer) renderer).setThirdPersonDistance(distance);
        }
    }

    protected void applyYawToEntity(Entity entityToUpdate) {
        entityToUpdate.setRenderYawOffset(this.rotationYaw);
        float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
        float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
        entityToUpdate.prevRotationYaw += f1 - f;
        entityToUpdate.rotationYaw += f1 - f;
        entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (this.canPassengerSteer() && this.lerpSteps > 0) {
            this.lerpSteps = 0;
            this.posX = this.lerpX;
            this.posY = this.lerpY;
            this.posZ = this.lerpZ;
            this.rotationYaw = (float) this.lerpYaw;
            this.rotationPitch = (float) this.lerpPitch;
        }
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger)) {
            float[] xOffset = getPlayerMountPositionXOffset(); // forward/backward
            float yOffset = (float)getMountedYOffset() + (float)passenger.getYOffset();
            float[] zOffset = getPlayerMountPositionZOffset(); // left/right

            int i = this.getPassengers().indexOf(passenger);

            Vec3d vec3d = (new Vec3d(xOffset[i], 0.0D, zOffset[i])).rotateYaw(-this.rotationYaw * ROTATION_FACTOR - ((float) Math.PI / 2F));
            passenger.setPosition(this.posX + vec3d.x, this.posY + (double) yOffset, this.posZ + vec3d.z);
            passenger.rotationYaw += this.deltaRotation;
            passenger.setRotationYawHead(passenger.getRotationYawHead() + this.deltaRotation);
            this.applyYawToEntity(passenger);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void applyOrientationToEntity(Entity entityToUpdate) {
        this.applyYawToEntity(entityToUpdate);
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYaw = (double) yaw;
        this.lerpPitch = (double) pitch;
        this.lerpSteps = 10;
    }

    @SideOnly(Side.CLIENT)
    public void updateInputs() {
        this.leftInputDown = Keybinds.rsLeft.isKeyDown();
        this.rightInputDown = Keybinds.rsRight.isKeyDown();
        this.forwardInputDown = Keybinds.rsForward.isKeyDown();
        this.backInputDown = Keybinds.rsBack.isKeyDown();
        this.upInputDown = Keybinds.rsUp.isKeyDown();
        this.downInputDown = Keybinds.rsDown.isKeyDown();
    }

    @SideOnly(Side.CLIENT)
    public void playSoundLoop() {
        if (!getSh().isSoundPlaying(sound) && world.isRemote) {
            getSh().playSound(sound);
        }
    }

    @Override
    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
        player.startRiding(this);
        return EnumActionResult.SUCCESS;
    }

    @Override
    protected void despawnEntity() {
        super.despawnEntity();
        getSh().stopSound(sound);
    }

    @Override
    protected boolean canBeRidden(Entity entityIn) {
        return entityIn instanceof EntityPlayer;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public double getMountedYOffset() {
        return getPlayerMountPositionYOffset();
    }

    @Override
    public boolean shouldDismountInWater(Entity rider){
        return false;
    }

    @Override
    protected boolean canFitPassenger(Entity passenger) {
        return this.getPassengers().size() < getMaxPassangers();
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    @Nullable
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return entityIn.canBePushed() ? entityIn.getEntityBoundingBox() : null;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }

    @Override
    public boolean canBeSteered() {
        return true;
    }

    public boolean isInAir() {
        int blockX = MathHelper.floor(posX);
        int blockY = MathHelper.floor(getEntityBoundingBox().minY) - 1;
        int blockZ = MathHelper.floor(posZ);

        return world.isAirBlock(new BlockPos(blockX, blockY, blockZ));
    }

    @Override
    public void fall(float distance, float damageMultiplier) {}

    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) { }

    @Override
    public EnumFacing getAdjustedHorizontalFacing() {
        return this.getHorizontalFacing().rotateY();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.isExplosion() || DamageSource.DROWN.getDamageType().equals(source.getDamageType())) {
            return false;
        }

        return super.attackEntityFrom(source, amount);
    }

    public OBJModel getModel() {
        return model;
    }
}
