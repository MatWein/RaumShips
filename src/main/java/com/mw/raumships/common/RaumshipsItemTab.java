package com.mw.raumships.common;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.common.blocks.ZPMHubBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RaumshipsItemTab extends CreativeTabs {
    public RaumshipsItemTab() {
        super(RaumShipsMod.MODID);
    }

    @Override
    public ItemStack getTabIconItem() {
        Block block = Block.getBlockFromName(RaumShipsMod.MODID + ":" + ZPMHubBlock.REGISTRY_NAME);
        Item item = Item.getItemFromBlock(block);

        return new ItemStack(item);
    }
}
