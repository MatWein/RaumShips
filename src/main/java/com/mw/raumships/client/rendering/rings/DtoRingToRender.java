package com.mw.raumships.client.rendering.rings;

import com.mw.raumships.client.rendering.model.Model;
import com.mw.raumships.client.rendering.model.ModelLoader;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Contains single instance of a transport ring
 *
 */
public class DtoRingToRender {
	
	private World world;
//	private int index;
	
	private boolean shouldRender;
	private boolean shouldAnimate;
	private boolean ringsUprising;
	private long animationStart;
		
	private double y;
	private double yMax;
	
	public DtoRingToRender(World world, int index) {
		this.world = world;
//		this.index = index;
		
		this.shouldRender = false;
		
		this.y = 0;
		this.yMax = 4-index + 1.5;
	}

	public void render(double partialTicks) {
//		ModelLoader.loadModel(EnumModel.RingsBlack);
		
		if (shouldRender) {
			Model ringModel = ModelLoader.getModel(ModelLoader.EnumModel.RingsBlack);
		
			if (ringModel != null) {
				ModelLoader.EnumModel.RingsBlack.bindTexture();
				
				GlStateManager.pushMatrix();				
				GlStateManager.translate(0, y, 0);
				
				ringModel.render();
				GlStateManager.popMatrix();
			}
		}
		
		if (shouldAnimate) {
			double effTick = world.getTotalWorldTime() - animationStart + partialTicks;
			
			effTick /= TransportRingsRenderer.animationDiv; // 2.7 = 1.8 * 1.5
//			effTick /= 1.8;
//			effTick /= 1.5;
			
			if (effTick > Math.PI) {
				effTick = Math.PI;
				shouldAnimate = false;
			}
			
			float cos = MathHelper.cos((float) effTick);
			
			if (ringsUprising)
				cos *= -1;
			
			y = ((cos + 1) / 2) * yMax;
			
			if (!ringsUprising && effTick == Math.PI)
				shouldRender = false;
		}
	}
	
	public void animate(boolean ringsUprising) {		
		this.ringsUprising = ringsUprising;
		shouldRender = true;
		
		animationStart = world.getTotalWorldTime();
		shouldAnimate = true;
	}

	public void setTop() {
		y = yMax;
		
		shouldAnimate = false;
		shouldRender = true;
	}

	public void setDown() {
		shouldAnimate = false;
		shouldRender = false;
	}
}
