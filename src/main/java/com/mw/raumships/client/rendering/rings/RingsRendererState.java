package com.mw.raumships.client.rendering.rings;

import io.netty.buffer.ByteBuf;

public class RingsRendererState extends RendererState {
    public boolean isAnimationActive;
    public long animationStart;
    public boolean ringsUprising;

    public RingsRendererState() {
        this.isAnimationActive = false;

        this.animationStart = 0;
        this.ringsUprising = true;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isAnimationActive);
        buf.writeLong(animationStart);
        buf.writeBoolean(ringsUprising);
    }

    @Override
    public RendererState fromBytes(ByteBuf buf) {
        isAnimationActive = buf.readBoolean();
        animationStart = buf.readLong();
        ringsUprising = buf.readBoolean();

        return this;
    }

    @Override
    protected String getKeyName() {
        return "rendererState";
    }
}
