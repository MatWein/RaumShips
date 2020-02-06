package com.mw.raumships.server.network;

import com.mw.raumships.client.gui.rings.State;
import com.mw.raumships.client.network.PositionedPlayerPacket;
import com.mw.raumships.client.network.StateUpdatePacketToClient;
import com.mw.raumships.common.blocks.rings.RingsTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import scala.NotImplementedError;

public class StateUpdateRequestToServer extends PositionedPlayerPacket {
    public StateUpdateRequestToServer() {
    }

    public StateUpdateRequestToServer(BlockPos pos, EntityPlayer player) {
        super(pos, player);
    }

    public static class StateUpdateServerHandler implements IMessageHandler<StateUpdateRequestToServer, IMessage> {
        @Override
        public StateUpdatePacketToClient onMessage(StateUpdateRequestToServer message, MessageContext ctx) {
            WorldServer world = ctx.getServerHandler().player.getServerWorld();

            if (world.isBlockLoaded(message.pos)) {
                world.addScheduledTask(() -> {
                    RingsTile te = (RingsTile) world.getTileEntity(message.pos);

                    if (te != null) {
                        try {
                            State state = te.getState();

                            if (state != null)
                                message.respond(world, new StateUpdatePacketToClient(message.pos, state));
                            else
                                throw new NotImplementedError("State not implemented on " + te.toString());
                        } catch (UnsupportedOperationException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            return null;
        }
    }
}
