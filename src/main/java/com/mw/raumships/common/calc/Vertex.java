package com.mw.raumships.common.calc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Vertex {
    public final float x;
    public final float y;
    public final float z;

    private float xRotated;
    private float yRotated;
    private float zRotated;

    private float xGlobal;
    private float yGlobal;
    private float zGlobal;

    public float xDiffrence;
    public float yDiffrence;
    public float zDiffrence;

    BlockPos oldGlobal = null;
    private Vec3d lastPlayerPos = new Vec3d(0, 0, 0);

    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return String.format("%f %f %f", x, y, z);
    }

    public Vertex rotate(double angle) {
        angle = Math.toRadians(angle);
        float sin = (float) Math.sin(angle);
        float cos = (float) Math.cos(angle);

        Matrix3f rotationMatrix = new Matrix3f();
        Matrix3f vertex = new Matrix3f();
        Matrix3f out = new Matrix3f();

        rotationMatrix.m00 = cos;
        rotationMatrix.m10 = -sin;
        rotationMatrix.m20 = 0;
        rotationMatrix.m01 = sin;
        rotationMatrix.m11 = cos;
        rotationMatrix.m21 = 0;
        rotationMatrix.m02 = 0;
        rotationMatrix.m12 = 0;
        rotationMatrix.m22 = 1;

        vertex.m00 = x;
        vertex.m01 = y;
        vertex.m02 = z;

        Matrix3f.mul(rotationMatrix, vertex, out);

        xRotated = out.m00;
        yRotated = out.m01;
        zRotated = out.m02;

        return this;
    }

    public Vertex localToGlobal(BlockPos pos, Vector3f translation) {
        if (!pos.equals(oldGlobal)) {
            xGlobal = xRotated + pos.getX() + translation.x;
            yGlobal = zRotated + pos.getY() + translation.y;
            zGlobal = pos.getZ() - yRotated + translation.z;

            oldGlobal = pos;
        }

        return this;
    }

    public Vertex calculateDiffrence(EntityPlayer player) {
        double posY = player.posY + player.getEyeHeight();

        if (lastPlayerPos.x != player.posX) xDiffrence = (float) (xGlobal - player.posX);
        if (lastPlayerPos.z != player.posZ) zDiffrence = (float) (zGlobal - player.posZ);
        if (lastPlayerPos.y != player.posY) yDiffrence = (float) (yGlobal - posY);

        lastPlayerPos = new Vec3d(player.posX, player.posY, player.posZ);

        return this;
    }

    public Vector2f getViewport(Vec3d lookVec) {
        double xy = xDiffrence / yDiffrence;
        double zy = zDiffrence / yDiffrence;

        float x = (float) (lookVec.y * xy);
        float z = (float) (lookVec.y * zy);

        return new Vector2f(x, z);
    }
}