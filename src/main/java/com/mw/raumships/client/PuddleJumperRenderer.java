package com.mw.raumships.client;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.common.PuddleJumperEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class PuddleJumperRenderer extends Render<PuddleJumperEntity> {
    public static final ResourceLocation MODEL = new ResourceLocation(RaumShipsMod.MODID, "models/entity/puddlejumper.obj");
    public static final ResourceLocation TEXTURE = new ResourceLocation(RaumShipsMod.MODID, "models/entity/puddlejumper.png");

    private OBJModel model;

    private float zVersatz = -1.5F;

    protected PuddleJumperRenderer(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(PuddleJumperEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (model == null) {
            try {
                model = (OBJModel) OBJLoader.INSTANCE.loadModel(MODEL);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);

        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.player;
        if (player != null && player.isRidingSameEntity(entity) && minecraft.gameSettings.thirdPersonView == 0) {
            double motionX = (double) (MathHelper.sin(-entityYaw * 0.017453292F) * zVersatz);
            double motionZ = (double) (MathHelper.cos(entityYaw * 0.017453292F) * zVersatz);
            GL11.glTranslatef((float)motionX, 0.0F, (float)motionZ);
        }
        GlStateManager.rotate(0.0F - entityYaw, 0.0F, 1.0F, 0.0F);


        GL11.glScalef(0.4f, 0.4f, 0.4f);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TEXTURE);
        ModelRenderer.renderObj(model);
        GL11.glPopMatrix();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(PuddleJumperEntity entity) {
        return null;
    }
}
