package com.mw.raumships.client.rendering.model;

import com.mw.raumships.RaumShipsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelLoader {
    private static Map<String, ResourceLocation> textureNameMap = new HashMap<>();
    private static List<String> failedToLoadTextures = new ArrayList<>();

    public enum EnumModel {
        RingsBlack("RingsBlack", "rings_black.obj", "rings/rings_black.png"),
        RingsController_goauld("RingsController_goauld", "plate_goauld.obj", "rings/goauld_panel.png"),
        RingsController_goauld_buttons("RingsController_goauld_buttons", "buttons_goauld.obj", "rings/goauld_buttons.png");

        private String name;
        private String modelPath;

        private ResourceLocation textureResource;

        EnumModel(String name, String path, String texturePath) {
            this.name = name;
            this.modelPath = "assets/" + RaumShipsMod.MODID + "/models/entity/" + path;

            if (texturePath != null) {
                this.textureResource = getTexture(texturePath);
            }
        }

        public String getName() {
            return name;
        }

        public String getPath() {
            return modelPath;
        }

        public void bindTexture() {
            if (this.textureResource != null) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(textureResource);
            }
        }
    }

    private static Map<String, Boolean> loadAttempted = new HashMap<>();
    private static Map<String, ModelLoaderThread> threads = new HashMap<>();

    public static Model getModel(EnumModel model) {
        String name = model.getName();

        try {
            return threads.get(name).getModel();
        } catch (NullPointerException e) {
            boolean loadAtt;

            try {
                loadAtt = loadAttempted.get(name);
            } catch (NullPointerException e2) {
                loadAtt = false;
            }

            if (!loadAtt) {
                loadModel(model);
                loadAttempted.put(name, true);
            }
            return null;
        }
    }

    public static void loadModel(EnumModel model) {
        String name = model.getName();

        threads.put(name, new ModelLoaderThread(model.getPath()));

        threads.get(name).setPriority(Thread.MIN_PRIORITY);
        threads.get(name).start();
    }

    public static ResourceLocation getTexture(String texturePath) {
        if (failedToLoadTextures.contains(texturePath))
            return null;

        ResourceLocation resource = textureNameMap.get(texturePath);

        if (resource == null) {
            resource = new ResourceLocation(RaumShipsMod.MODID + ":textures/tesr/" + texturePath);

            if (!Minecraft.getMinecraft().getTextureManager().loadTexture(resource, new SimpleTexture(resource))) {
                failedToLoadTextures.add(texturePath);

                return null;
            }

            textureNameMap.put(texturePath, resource);
        }

        return resource;
    }

}
