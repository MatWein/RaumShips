package com.mw.raumships.client.rendering.rings;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTTagCompound;

public abstract class RendererState {
	public RendererState() {}
	
	public abstract void toBytes(ByteBuf buf);
	
	public abstract RendererState fromBytes(ByteBuf buf);
	
	protected String getKeyName() {
		return "rendererState";
	}
	
	public void toNBT(NBTTagCompound compound) {
		ByteBuf buf = Unpooled.buffer();
		toBytes(buf);
		
		byte[] dst = new byte[buf.readableBytes()];
		buf.readBytes(dst);
		
		compound.setByteArray(getKeyName(), dst);
	}
	
	public void fromNBT(NBTTagCompound compound) {
		byte[] dst = compound.getByteArray(getKeyName());
		
		if (dst != null && dst.length > 0) {
			ByteBuf buf = Unpooled.copiedBuffer(dst);
			fromBytes(buf);
		}
	}
}
