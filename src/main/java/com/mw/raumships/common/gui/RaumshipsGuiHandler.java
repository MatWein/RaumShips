package com.mw.raumships.common.gui;

import com.mw.raumships.client.gui.zpm.ZpmHubGuiContainer;
import com.mw.raumships.common.blocks.ZPMHubTileEntity;
import com.mw.raumships.server.gui.zpm.ZpmHubContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class RaumshipsGuiHandler implements IGuiHandler {
    public static final int GUI_ID_ZPM_HUB = 1001;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos xyz = new BlockPos(x, y, z);
        TileEntity tileEntity = world.getTileEntity(xyz);
        if (ID == GUI_ID_ZPM_HUB && tileEntity instanceof ZPMHubTileEntity) {
            ZPMHubTileEntity zpmHubTileEntity = (ZPMHubTileEntity) tileEntity;
            return new ZpmHubContainer(player.inventory, zpmHubTileEntity);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos xyz = new BlockPos(x, y, z);
        TileEntity tileEntity = world.getTileEntity(xyz);
        if (ID == GUI_ID_ZPM_HUB && tileEntity instanceof ZPMHubTileEntity) {
            ZPMHubTileEntity zpmHubTileEntity = (ZPMHubTileEntity) tileEntity;
            return new ZpmHubGuiContainer(player.inventory, zpmHubTileEntity);
        }

        return null;
    }
}
