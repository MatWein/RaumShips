package com.mw.raumships.client.gui.rings;

import com.mw.raumships.client.rendering.rings.RendererState;

/**
 * Defines {@link RendererState} of which type we want to request from the server.
 * This will be sent to server. Then, appropriate state will be serialized and
 * returned to the client(Based on {@link ITileEntityStateProvider#getState(EnumStateType)}). Deserialization will occur based on this Enum.
 * 
 * @author MrJake
 *
 */
public enum EnumStateType {
	// This IDs MUST BE in order
	// Also NBT keys must be unique
	GUI_STATE(0);
	
	public int id;

	EnumStateType(int id) {
		this.id = id;
	}

	public static EnumStateType byId(int id) {
		return EnumStateType.values()[id];
	}
}
