package com.mw.raumships.client.rendering.rings;

public interface ISpecialRenderer<STATE extends RendererState> {
	void render(double x, double y, double z, double partialTicks);
	
	void setState(STATE rendererState);
}
