package com.mw.raumships.server.gui.zpm;

import com.mw.raumships.common.items.ZPMItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ZpmHubContainerSlot extends Slot {
    public ZpmHubContainerSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof ZPMItem;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }
}
