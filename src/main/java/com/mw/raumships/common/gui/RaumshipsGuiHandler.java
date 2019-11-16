package com.mw.raumships.common.gui;

import com.mw.raumships.client.gui.zpm.ZpmHubGuiContainer;
import com.mw.raumships.common.blocks.ZPMChargerTileEntity;
import com.mw.raumships.common.blocks.ZPMHubTileEntity;
import com.mw.raumships.server.gui.zpm.ZpmHubContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class RaumshipsGuiHandler implements IGuiHandler {
    public static final int GUI_ID_ZPM_HUB = 1001;
    public static final int GUI_ID_ZPM_CHARGER = 1002;

    @Override
    public Object getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
        BlockPos xyz = new BlockPos(x, y, z);
        TileEntity tileEntity = world.getTileEntity(xyz);

        if (ID == GUI_ID_ZPM_HUB && tileEntity instanceof ZPMHubTileEntity) {
            ZPMHubTileEntity zpmHubTileEntity = (ZPMHubTileEntity) tileEntity;
            return new ZpmHubContainer(player.inventory, zpmHubTileEntity);
        } else if (ID == GUI_ID_ZPM_CHARGER && tileEntity instanceof ZPMChargerTileEntity) {
            ZPMChargerTileEntity zpmChargerTileEntity = (ZPMChargerTileEntity) tileEntity;
            return new ZpmHubContainer(player.inventory, zpmChargerTileEntity);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
        BlockPos xyz = new BlockPos(x, y, z);
        TileEntity tileEntity = world.getTileEntity(xyz);

        if (ID == GUI_ID_ZPM_HUB && tileEntity instanceof ZPMHubTileEntity) {
            ZPMHubTileEntity zpmHubTileEntity = (ZPMHubTileEntity) tileEntity;
            return new ZpmHubGuiContainer(player.inventory, zpmHubTileEntity);
        } else if (ID == GUI_ID_ZPM_CHARGER && tileEntity instanceof ZPMChargerTileEntity) {
            ZPMChargerTileEntity zpmChargerTileEntity = (ZPMChargerTileEntity) tileEntity;
            return new ZpmHubGuiContainer(player.inventory, zpmChargerTileEntity);
        }

        return null;
    }
}
