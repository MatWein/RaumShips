package com.mw.raumships.client.gui.rings;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Implemented by {@link TileEntity} which provides at least one {@link State}
 * 
 * @author MrJake
 */
public interface ITileEntityStateProvider {
	
	/**
	 * Server-side method. Called on {@link TileEntity} to get specified {@link State}.
	 * 
	 * @param stateType {@link EnumStateType} State to be collected/returned
	 * @return {@link State} instance
	 */
	public abstract State getState(EnumStateType stateType);
	
	/**
	 * Client-side method. Called on {@link TileEntity} to get specified {@link State} instance
	 * to recreate State by deserialization
	 * 
	 * @param stateType {@link EnumStateType} State to be deserialized
	 * @return deserialized {@link State}
	 */
	public abstract State createState(EnumStateType stateType);

	/**
	 * Client-side method. Sets appropriate fields in client-side tile entity for it
	 * to mirror the server-side tile entity
	 * 
	 * @param stateType {@link EnumStateType} State to be applied
	 * @param state {@link State} instance obtained from packet
	 */
	@SideOnly(Side.CLIENT)
	public abstract void setState(EnumStateType stateType, State state);
}
