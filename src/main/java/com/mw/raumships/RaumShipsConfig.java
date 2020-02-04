package com.mw.raumships;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;

@Config(modid = RaumShipsMod.MODID, name = RaumShipsMod.NAME)
public class RaumShipsConfig {
    @Name("Transport rings options")
    public static RingsConfig ringsConfig = new RingsConfig();

    public static class RingsConfig {
        @Name("Rings range's radius horizontal")
        @RangeInt(min = 1, max = 256)
        public int rangeFlat = 25;

        @Name("Rings vertical reach")
        @RangeInt(min = 1, max = 256)
        public int rangeVertical = 256;

        @Name("Ignore rings check for blocks to replace")
        public boolean ignoreObstructionCheck = false;
    }
}
