package com.mw.raumships.client;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.Face;
import net.minecraftforge.client.model.obj.OBJModel.Group;
import net.minecraftforge.client.model.obj.OBJModel.Vertex;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class ModelRenderer {
    public static void renderObj(OBJModel model) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Collection<Group> groups = model.getMatLib().getGroups().values();
        Iterator<Group> iterGroup = groups.iterator();

        renderGroups(vertexbuffer, iterGroup);

        tessellator.draw();
    }

    private static void renderGroups(BufferBuilder vertexbuffer, Iterator<Group> iterGroup) {
        vertexbuffer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX_NORMAL);
        while (iterGroup.hasNext()) {
            Group g = iterGroup.next();
            Set<Face> faces = g.getFaces();

            for (Face f : faces) {
                Vertex[] vertices = f.getVertices();
                float offset = 0.0005F;
                Vector3f faceNormal = calculateFaceNormal(vertices[0], vertices[1], vertices[2]);

                OBJModel.TextureCoordinate tc0 = vertices[0].getTextureCoordinate();
                OBJModel.TextureCoordinate tc1 = vertices[1].getTextureCoordinate();
                OBJModel.TextureCoordinate tc2 = vertices[2].getTextureCoordinate();
                float averageU = (tc0.u + tc1.u + tc2.u) / 3.0F;
                float averageV = (tc0.v + tc1.v + tc2.v) / 3.0F;

                float offsetU = offset;
                float offsetV = offset;
                Vector4f pos = vertices[0].getPos();
                if (tc0.u > averageU) offsetU = -offsetU;
                if (tc0.v > averageV) offsetV = -offsetV;
                vertexbuffer.pos(pos.x, pos.y, pos.z).tex(tc0.u + offsetU, 1.0F - (tc0.v + offsetV)).normal(faceNormal.x, faceNormal.y, faceNormal.z).endVertex();
                offsetU = offset;
                offsetV = offset;
                pos = vertices[1].getPos();
                if (tc1.u > averageU) offsetU = -offsetU;
                if (tc1.v > averageV) offsetV = -offsetV;
                vertexbuffer.pos(pos.x, pos.y, pos.z).tex(tc1.u + offsetU, 1.0F - (tc1.v + offsetV)).normal(faceNormal.x, faceNormal.y, faceNormal.z).endVertex();
                offsetU = offset;
                offsetV = offset;
                pos = vertices[2].getPos();
                if (tc2.u > averageU) offsetU = -offsetU;
                if (tc2.v > averageV) offsetV = -offsetV;
                vertexbuffer.pos(pos.x, pos.y, pos.z).tex(tc2.u + offsetU, 1.0F - (tc2.v + offsetV)).normal(faceNormal.x, faceNormal.y, faceNormal.z).endVertex();
            }
        }
    }

    private static Vector3f calculateFaceNormal(Vertex v0, Vertex v1, Vertex v2) {
        Vector4f pos0 = v0.getPos();
        Vector4f pos1 = v1.getPos();
        Vector4f pos2 = v2.getPos();
        Vec3d vec1 = new Vec3d(pos1.x - pos0.x, pos1.y - pos0.y, pos1.z - pos0.z);
        Vec3d vec2 = new Vec3d(pos2.x - pos0.x, pos2.y - pos0.y, pos2.z - pos0.z);
        Vec3d normalVector = vec1.crossProduct(vec2).normalize();

        return new Vector3f((float) normalVector.x, (float) normalVector.y, (float) normalVector.z);
    }
}
