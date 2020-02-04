package com.mw.raumships.common.items;

import com.mw.raumships.RaumShipsMod;
import net.minecraft.item.Item;

public class AnalyzerAncientItem extends Item {
    private static final String REGISTRY_NAME = "analyzer_ancient";

    public AnalyzerAncientItem() {
        setUnlocalizedName(REGISTRY_NAME);
        setRegistryName(RaumShipsMod.MODID, REGISTRY_NAME);
    }
}
