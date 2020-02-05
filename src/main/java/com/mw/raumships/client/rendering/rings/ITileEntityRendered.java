package com.mw.raumships.client.rendering.rings;

import io.netty.buffer.ByteBuf;

public interface ITileEntityRendered {
	ISpecialRenderer getRenderer();
	
	RendererState getRendererState();
	
	RendererState createRendererState(ByteBuf buf);
}
