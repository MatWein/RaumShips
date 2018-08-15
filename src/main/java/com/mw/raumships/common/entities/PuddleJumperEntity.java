package com.mw.raumships.common.entities;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.Keybinds;
import com.mw.raumships.client.sound.FlyingEntitySoundLoop;
import com.mw.raumships.common.interfaces.IEntityWithModel;
import com.mw.raumships.common.interfaces.IEntityWithProperties;
import com.mw.raumships.common.interfaces.IFlyingEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.SoundHandler;
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

import static com.mw.raumships.common.RSCommonConstants.DEFAULT_MINECRAFT_VIEW_DISTANCE;
import static com.mw.raumships.common.RSCommonConstants.ROTATION_FACTOR;
import static com.mw.raumships.common.entities.PuddleJumperEntityConstants.*;

public class PuddleJumperEntity extends EntityLiving implements IEntityWithModel, IEntityWithProperties, IFlyingEntity {
    private float deltaRotation;

    private boolean leftInputDown;
    private boolean rightInputDown;
    private boolean forwardInputDown;
    private boolean backInputDown;
    private boolean upInputDown;
    private boolean downInputDown;

    private int lerpSteps;
    private double lerpPitch;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYaw;

    private final SoundHandler soundHandler;
    private final FlyingEntitySoundLoop<PuddleJumperEntity> sound;
    private final OBJModel model;

    public PuddleJumperEntity(World worldIn) {
        super(worldIn);

        setSize(BOUNDING_BOX_WIDTH, BOUNDING_BOX_HEIGHT);

        this.ignoreFrustumCheck = true;
        this.preventEntitySpawning = true;
        this.soundHandler = RaumShipsMod.mc.getSoundHandler();
        this.sound = new FlyingEntitySoundLoop<>(this);
        try {
            this.model = (OBJModel) OBJLoader.INSTANCE.loadModel(MODEL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
                this.updateThirdPersonDistance(THIRD_PERSON_DISTANCE);
                this.updatePlayerCamera();
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        } else {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
        }

        this.doBlockCollisions();

        EntityPlayerSP player = RaumShipsMod.mc.player;
        if (player != null && player.isSneaking()) {
            this.updateThirdPersonDistance(DEFAULT_MINECRAFT_VIEW_DISTANCE);
            RaumShipsMod.mc.gameSettings.hideGUI = false;
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
        this.motionX *= MOMENTUM;
        this.motionY *= MOMENTUM;
        this.motionZ *= MOMENTUM;
        this.deltaRotation *= MOMENTUM;
    }

    public void controlAirship() {
        if (this.isBeingRidden()) {
            float f = 0.0F;
            float f1 = 0.0F;

            if (this.leftInputDown) {
                this.deltaRotation -= (FINAL_AIRSHIP_SPEED_TURN + (SPEED_MODIFIER * 2));
            }

            if (this.rightInputDown) {
                this.deltaRotation += (FINAL_AIRSHIP_SPEED_TURN + (SPEED_MODIFIER * 2));
            }

            if (this.rightInputDown != this.leftInputDown && !this.forwardInputDown && !this.backInputDown) {
                f += 0.005F;
            }

            this.rotationYaw += this.deltaRotation;

            if (this.forwardInputDown) {
                f += FINAL_AIRSHIP_SPEED_FORWARD;
            }

            if (this.backInputDown) {
                f -= 0.005F;
            }

            if (this.upInputDown) {
                f1 += FINAL_AIRSHIP_SPEED_UP + (SPEED_MODIFIER / 32);
            }

            if (this.downInputDown) {
                f1 -= FINAL_AIRSHIP_SPEED_DOWN + (SPEED_MODIFIER / 32);
            }

            this.motionX += (double) (MathHelper.sin(-this.rotationYaw * ROTATION_FACTOR) * f);
            this.motionZ += (double) (MathHelper.cos(this.rotationYaw * ROTATION_FACTOR) * f);
            this.motionY += (double) (3.017453292F * f1);

            this.rotationPitch += 10;
        }
    }

    @SideOnly(Side.CLIENT)
    public void updatePlayerCamera() {
        EntityPlayerSP player = RaumShipsMod.mc.player;
        if (player != null && RaumShipsMod.mc.gameSettings.thirdPersonView == 0) {
            player.rotationYaw = this.rotationYaw;
            player.rotationPitch = 0.0F;
            RaumShipsMod.mc.gameSettings.hideGUI = true;
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateThirdPersonDistance(float distance) {
        EntityRenderer renderer = RaumShipsMod.mc.entityRenderer;
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
            float zOffset = 0.0F; // left/right
            float xOffset = 2.0F; // forward/backward
            float yOffset = (float) ((this.isDead ? 0.009999999776482582D : this.getMountedYOffset()) + passenger.getYOffset());

            if (this.getPassengers().size() > 1) {
                int i = this.getPassengers().indexOf(passenger);

                if (i == 0) {
                    zOffset = 0.0F;
                } else {
                    zOffset = 0.0F;
                }
            }

            Vec3d vec3d = (new Vec3d(xOffset, 0.0D, zOffset)).rotateYaw(-this.rotationYaw * ROTATION_FACTOR - ((float) Math.PI / 2F));
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
        if (!soundHandler.isSoundPlaying(sound) && world.isRemote) {
            soundHandler.playSound(sound);
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
        soundHandler.stopSound(sound);
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
        return 0.1;
    }

    @Override
    public boolean shouldDismountInWater(Entity rider){
        return false;
    }

    @Override
    protected boolean canFitPassenger(Entity passenger) {
        return this.getPassengers().size() < MAX_PASSANGERS;
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
    public OBJModel getModel() {
        return model;
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    public float getRenderCockpitCameraZOffset() {
        return COCKPIT_CAMERA_Z_OFFSET;
    }

    @Override
    public float getRenderScalingFactor() {
        return SCALING_FACTOR;
    }
}
