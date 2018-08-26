package com.mw.raumships.common.entities;

import com.mw.raumships.client.Keybinds;
import com.mw.raumships.client.sound.Sounds;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static com.mw.raumships.client.ClientUtils.getMc;
import static com.mw.raumships.client.ClientUtils.isEgoPersonView;
import static com.mw.raumships.common.RSCommonConstants.*;

public abstract class RaumShipsEntity extends EntityLiving  {
    protected float deltaRotation;

    protected boolean leftInputDown;
    protected boolean rightInputDown;
    protected boolean forwardInputDown;
    protected boolean backInputDown;
    protected boolean upInputDown;
    protected boolean downInputDown;
    protected boolean ctrlInputDown;

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

        if (this.world.isRemote) {
            EntityPlayerSP player = getMc().player;
            if (player != null && player.isSneaking()) {
                player.dismountRidingEntity();
                getMc().gameSettings.hideGUI = false;
                this.updateThirdPersonDistance(DEFAULT_MINECRAFT_VIEW_DISTANCE);
            }
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
            float forwardBackward = 0.0F;
            float upDown = 0.0F;
            float leftRight = 0.0F;

            if (this.leftInputDown) {
                if (this.ctrlInputDown) {
                    leftRight -= getFinalAirShipSpeedDown() + (getSpeedModifier() / 32);
                } else {
                    this.deltaRotation -= (getFinalAirShipSpeedTurn() + (getSpeedModifier() * 2));
                }
            }

            if (this.rightInputDown) {
                if (this.ctrlInputDown) {
                    leftRight += getFinalAirShipSpeedDown() + (getSpeedModifier() / 32);
                } else {
                    this.deltaRotation += (getFinalAirShipSpeedTurn() + (getSpeedModifier() * 2));
                }
            }

            this.rotationYaw += this.deltaRotation;

            if (this.forwardInputDown) {
                forwardBackward += getFinalAirShipSpeedForward();
            }

            if (this.backInputDown) {
                forwardBackward -= 0.005F;
            }

            if (this.upInputDown) {
                upDown += getFinalAirShipSpeedUp() + (getSpeedModifier() / 32);
            }

            if (this.downInputDown) {
                upDown -= getFinalAirShipSpeedDown() + (getSpeedModifier() / 32);
            }

            if (this.ctrlInputDown) {
                float factor = leftRight >= 0.0F ? 1.0F : -1.0F;
                this.motionZ += (double) (MathHelper.sin(-this.rotationYaw * ROTATION_FACTOR) * Math.abs(leftRight)) * factor;
                this.motionX += (double) (MathHelper.cos(this.rotationYaw * ROTATION_FACTOR) * Math.abs(leftRight)) * -factor;
            } else {
                this.motionX += (double) (MathHelper.sin(-this.rotationYaw * ROTATION_FACTOR) * forwardBackward);
                this.motionZ += (double) (MathHelper.cos(this.rotationYaw * ROTATION_FACTOR) * forwardBackward);
            }

            this.motionY += (double) (Y_FACTOR * upDown);
        }
    }

    public void updatePlayerCamera() {
        EntityPlayerSP player = getMc().player;

        if (player != null && isEgoPersonView()) {
            player.rotationYaw = this.rotationYaw;
            player.rotationPitch = 0.0F;
            getMc().gameSettings.hideGUI = true;
        }
    }

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

    @Override
    @SideOnly(Side.CLIENT)
    public void applyOrientationToEntity(Entity entityToUpdate) {
        this.applyYawToEntity(entityToUpdate);
    }

    @Override
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
        this.downInputDown = Keybinds.rsDown.isKeyDown();
        this.ctrlInputDown = Keybinds.rsCtrl.isKeyDown();
    }

    public void playSoundLoop() {
        if (world.isRemote) {
            Sounds.playSound(this, getSound(), getVolume());
        }
    }

    @Override
    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
        player.startRiding(this);
        return EnumActionResult.SUCCESS;
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

    @Override
    public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {}
}
