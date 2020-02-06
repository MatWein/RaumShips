package com.mw.raumships.client.rendering.rings;

import com.mw.raumships.client.rendering.model.Model;
import com.mw.raumships.client.rendering.model.ModelLoader;
import com.mw.raumships.common.RSCommonConstants;
import com.mw.raumships.common.blocks.rings.RingsControllerTile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.util.vector.Vector3f;

public class RingsControllerRenderer {
    private EnumFacing facing;

    public RingsControllerRenderer(RingsControllerTile controllerTile) {
        World world = controllerTile.getWorld();
        BlockPos pos = controllerTile.getPos();

        IBlockState blockState = world.getBlockState(pos);
        facing = blockState.getValue(RSCommonConstants.FACING_HORIZONTAL);
    }

    private static final Vector3f NORTH_TRANSLATION = new Vector3f(0, 0, 0);
    private static final Vector3f EAST_TRANSLATION = new Vector3f(1, 0, 0);
    private static final Vector3f SOUTH_TRANSLATION = new Vector3f(1, 0, 1);
    private static final Vector3f WEST_TRANSLATION = new Vector3f(0, 0, 1);

    public static Vector3f getTranslation(EnumFacing facing) {
        switch (facing) {
            case NORTH:
                return NORTH_TRANSLATION;

            case EAST:
                return EAST_TRANSLATION;

            case SOUTH:
                return SOUTH_TRANSLATION;

            case WEST:
                return WEST_TRANSLATION;

            default:
                return null;
        }
    }

    public static int getRotation(EnumFacing facing) {
        switch (facing) {
            case NORTH:
                return 0;

            case EAST:
                return 270;

            case SOUTH:
                return 180;

            case WEST:
                return 90;

            default:
                return 0;
        }
    }

    public void render(double x, double y, double z, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        Vector3f tr = getTranslation(facing);
        int rot = getRotation(facing);

        GlStateManager.translate(tr.x, tr.y, tr.z);
        GlStateManager.rotate(rot, 0, 1, 0);

        ModelLoader.EnumModel.RingsController_goauld.bindTexture();
        Model model = ModelLoader.getModel(ModelLoader.EnumModel.RingsController_goauld);
        if (model != null)
            model.render();

        ModelLoader.EnumModel.RingsController_goauld_buttons.bindTexture();
        model = ModelLoader.getModel(ModelLoader.EnumModel.RingsController_goauld_buttons);
        if (model != null)
            model.render();

        GlStateManager.popMatrix();
    }
}
