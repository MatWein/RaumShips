package com.mw.raumships.common.blocks.rings;

import com.mw.raumships.RaumShipsMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;

public class InvisibleBlock extends Block {

	private static final String REGISTRY_NAME = "invisible_block";
	
	public InvisibleBlock() {
		super(Material.AIR);

		setUnlocalizedName(REGISTRY_NAME);
		setRegistryName(RaumShipsMod.MODID, REGISTRY_NAME);
		setLightLevel(1.0f);
		setLightOpacity(0);
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
}
