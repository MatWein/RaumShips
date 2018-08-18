package com.mw.raumships.client.rendering;

import com.mw.raumships.common.entities.RaumShipsEntity;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

import static com.mw.raumships.client.ClientUtils.getMc;
import static com.mw.raumships.common.RSCommonConstants.ROTATION_FACTOR;

public class EntityWithModelRenderer extends Render<RaumShipsEntity> {
    public EntityWithModelRenderer(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(RaumShipsEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y + entity.getRenderYOffset(), (float) z);

        EntityPlayerSP player = getMc().player;
        if (player != null && player.isRidingSameEntity(entity) && getMc().gameSettings.thirdPersonView == 0) {
            double motionX = (double) (MathHelper.sin(-entityYaw * ROTATION_FACTOR) * entity.getRenderCockpitCameraZOffset());
            double motionZ = (double) (MathHelper.cos(entityYaw * ROTATION_FACTOR) * entity.getRenderCockpitCameraZOffset());
            GL11.glTranslatef((float)motionX, 0.0F, (float)motionZ);
        }
        GlStateManager.rotate(0.0F - entityYaw, 0.0F, 1.0F, 0.0F);

        GL11.glScalef(entity.getRenderScalingFactor(), entity.getRenderScalingFactor(), entity.getRenderScalingFactor());
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(entity.getTextureResourceLocation());
        ModelRenderer.renderObj(entity.getModel());
        GL11.glPopMatrix();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(RaumShipsEntity entity) {
        return null;
    }
}
