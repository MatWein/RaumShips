package com.mw.raumships.client.network;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.rendering.rings.RendererState;
import com.mw.raumships.client.rendering.rings.RingsRendererState;
import com.mw.raumships.common.blocks.rings.RingsTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RendererUpdatePacketToClient extends PositionedPacket {
    public RendererUpdatePacketToClient() {
    }

    private RendererState rendererState;

    private ByteBuf stateBuf;

    public RendererUpdatePacketToClient(BlockPos pos, RendererState rendererState) {
        super(pos);

        this.rendererState = rendererState;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);

        rendererState.toBytes(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);

        stateBuf = buf.copy();
    }

    public static class TileUpdateClientHandler implements IMessageHandler<RendererUpdatePacketToClient, IMessage> {
        @Override
        public IMessage onMessage(RendererUpdatePacketToClient message, MessageContext ctx) {
            EntityPlayer player = RaumShipsMod.proxy.getPlayerClientSide();
            World world = player.getEntityWorld();

            RaumShipsMod.proxy.addScheduledTaskClientSide(() -> {
                RingsTile te = (RingsTile) world.getTileEntity(message.pos);
                RingsRendererState rendererState = (RingsRendererState)te.createRendererState(message.stateBuf);
                te.getRenderer().setState(rendererState);
            });

            return null;
        }

    }
}
