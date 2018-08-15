package com.mw.raumships.common.interfaces;

import net.minecraft.util.ResourceLocation;

public interface IEntityWithProperties {
    ResourceLocation getTexture();

    float getRenderCockpitCameraZOffset();
    float getRenderScalingFactor();
}
