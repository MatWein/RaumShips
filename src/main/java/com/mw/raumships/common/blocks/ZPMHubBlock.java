package com.mw.raumships.common.blocks;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.common.RSCommonConstants;
import com.mw.raumships.common.gui.RaumshipsGuiHandler;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ZPMHubBlock extends BlockContainer {
    public static final String REGISTRY_NAME = "zpmhub";

    private static final PropertyBool PROP_ENABLED = PropertyBool.create("enabled");

    public ZPMHubBlock() {
        super(Material.ROCK);

        setUnlocalizedName(REGISTRY_NAME);
        setRegistryName(RaumShipsMod.MODID, REGISTRY_NAME);

        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }

        playerIn.openGui(RaumShipsMod.instance, RaumshipsGuiHandler.GUI_ID_ZPM_HUB, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new ZPMHubTileEntity();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof IInventory) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileEntity);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof ZPMHubTileEntity) {
            ZPMHubTileEntity zpmHubTileEntity = (ZPMHubTileEntity)tileEntity;
            return getDefaultState().withProperty(PROP_ENABLED, zpmHubTileEntity.hasActiveZpm());
        }

        return state;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.SOLID;
    }

    @Override
    public boolean isOpaqueCube(IBlockState iBlockState) {
        return true;
    }

    @Override
    public boolean isFullCube(IBlockState iBlockState) {
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PROP_ENABLED);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        IBlockState blockState = getActualState(getDefaultState(), world, pos);
        Boolean enabled = blockState.getValue(PROP_ENABLED);
        return Boolean.TRUE.equals(enabled) ? RSCommonConstants.MAX_LIGHT : RSCommonConstants.MIN_LIGHT;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (Boolean.TRUE.equals(state.getValue(PROP_ENABLED))) {
            return 1;
        }

        return 0;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
        return EnumBlockRenderType.MODEL;
    }
}
