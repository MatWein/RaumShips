package com.mw.raumships.common;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.EnumFacing;

import java.util.Arrays;

public interface RSCommonConstants {
    float DEFAULT_MINECRAFT_VIEW_DISTANCE = 4.0F;

    float ROTATION_FACTOR = 0.017453292F;
    float Y_FACTOR = 3.017453292F;

    int MAX_LIGHT = 15;
    int MIN_LIGHT = 0;

    PropertyDirection FACING_HORIZONTAL = PropertyDirection.create("facing", Arrays.asList(EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST));

    int RENDER_DISTANCE = 200;
}
