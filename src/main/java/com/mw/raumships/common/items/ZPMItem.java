package com.mw.raumships.common.items;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.gui.zpm.ZpmHubGuiContainer;
import com.mw.raumships.common.RaumshipsItemTab;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ZPMItem extends Item {
    public static final int MAX_ZPM_ENERGY_FACTOR = 250000000;
    public static final int MAX_ZPM_ENERGY = 7 * MAX_ZPM_ENERGY_FACTOR;

    public static final String REGISTRY_NAME = "zpm";
    public static final String NBT_ZPM = "ZPM";
    public static final String KEY_ZPM_ENERGY = "zpmEnergy";

    public ZPMItem() {
        setUnlocalizedName(REGISTRY_NAME);
        setRegistryName(RaumShipsMod.MODID, REGISTRY_NAME);

        setMaxStackSize(1);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if(!worldIn.isRemote) {
            ItemStack activeItemStack = playerIn.getHeldItemMainhand();
            int zpmEnergy = getZpmEnergy(activeItemStack);
            int percentage = (int) (zpmEnergy * 100.0 / ZPMItem.MAX_ZPM_ENERGY);
            String number = ZpmHubGuiContainer.NUMBER_FORMAT.format(zpmEnergy);
            String message = String.format("ZPM: %s RF (%s %%)", number, percentage);

            Minecraft.getMinecraft().player.sendChatMessage(message);
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public static int getZpmEnergy(ItemStack stackInSlot) {
        if (stackInSlot == null) {
            return 0;
        }

        if (!(stackInSlot.getItem() instanceof ZPMItem)) {
            return 0;
        }

        return stackInSlot.getOrCreateSubCompound(ZPMItem.NBT_ZPM).getInteger(ZPMItem.KEY_ZPM_ENERGY);
    }

    public static void setZpmEnergy(ItemStack stackInSlot, int zpmEnergy) {
        if (stackInSlot == null) {
            return;
        }

        if (!(stackInSlot.getItem() instanceof ZPMItem)) {
            return;
        }

        int newZpmEnergy = Math.max(zpmEnergy, 0);
        stackInSlot.getOrCreateSubCompound(ZPMItem.NBT_ZPM).setInteger(ZPMItem.KEY_ZPM_ENERGY, newZpmEnergy);

        if (newZpmEnergy >= 6 * MAX_ZPM_ENERGY_FACTOR) {
            stackInSlot.setItemDamage(0);
        } else if (newZpmEnergy >= 5 * MAX_ZPM_ENERGY_FACTOR) {
            stackInSlot.setItemDamage(1);
        } else if (newZpmEnergy >= 4 * MAX_ZPM_ENERGY_FACTOR) {
            stackInSlot.setItemDamage(2);
        } else if (newZpmEnergy >= 3 * MAX_ZPM_ENERGY_FACTOR) {
            stackInSlot.setItemDamage(3);
        } else if (newZpmEnergy >= 2 * MAX_ZPM_ENERGY_FACTOR) {
            stackInSlot.setItemDamage(4);
        } else if (newZpmEnergy >= MAX_ZPM_ENERGY_FACTOR) {
            stackInSlot.setItemDamage(5);
        } else if (newZpmEnergy > 0) {
            stackInSlot.setItemDamage(6);
        } else {
            stackInSlot.setItemDamage(7);
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab instanceof RaumshipsItemTab) {
            items.add(createItemStackWithProperty(0, MAX_ZPM_ENERGY));
            items.add(createItemStackWithProperty(1, 6 * MAX_ZPM_ENERGY_FACTOR));
            items.add(createItemStackWithProperty(2, 5 * MAX_ZPM_ENERGY_FACTOR));
            items.add(createItemStackWithProperty(3, 4 * MAX_ZPM_ENERGY_FACTOR));
            items.add(createItemStackWithProperty(4, 3 * MAX_ZPM_ENERGY_FACTOR));
            items.add(createItemStackWithProperty(5, 2 * MAX_ZPM_ENERGY_FACTOR));
            items.add(createItemStackWithProperty(6, MAX_ZPM_ENERGY_FACTOR));
            items.add(createItemStackWithProperty(7, 0));
        }
    }

    private ItemStack createItemStackWithProperty(int meta, int zpmEnergy) {
        ItemStack itemStack = new ItemStack(this, 1, meta);
        setZpmEnergy(itemStack, zpmEnergy);
        return itemStack;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }
}
