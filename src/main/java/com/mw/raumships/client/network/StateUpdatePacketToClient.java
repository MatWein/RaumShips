package com.mw.raumships.client.network;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.gui.rings.State;
import com.mw.raumships.common.blocks.rings.RingsTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StateUpdatePacketToClient extends PositionedPacket {
    private State state;
    private ByteBuf stateBuf;

    public StateUpdatePacketToClient() {
    }

    public StateUpdatePacketToClient(BlockPos pos, State state) {
        super(pos);

        this.state = state;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);

        state.toBytes(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);

        stateBuf = buf.copy();
    }

    public static class StateUpdateClientHandler implements IMessageHandler<StateUpdatePacketToClient, IMessage> {
        @Override
        public IMessage onMessage(StateUpdatePacketToClient message, MessageContext ctx) {
            EntityPlayer player = RaumShipsMod.proxy.getPlayerClientSide();
            World world = player.getEntityWorld();

            RaumShipsMod.proxy.addScheduledTaskClientSide(() -> {
                RingsTile te = (RingsTile) world.getTileEntity(message.pos);

                try {
                    State state = te.createState();
                    state.fromBytes(message.stateBuf);

                    if (te != null && state != null) {
                        te.setState(state);
                    }
                } catch (UnsupportedOperationException e) {
                    e.printStackTrace();
                }
            });

            return null;
        }

    }
}
