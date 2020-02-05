package com.mw.raumships.server.network;

import com.mw.raumships.client.gui.rings.EnumStateType;
import com.mw.raumships.client.gui.rings.State;
import com.mw.raumships.client.network.PositionedPlayerPacket;
import com.mw.raumships.client.network.StateUpdatePacketToClient;
import com.mw.raumships.common.blocks.rings.RingsTile;
import io.netty.buffer.ByteBuf;
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

    EnumStateType stateType;

    public StateUpdateRequestToServer(BlockPos pos, EntityPlayer player, EnumStateType stateType) {
        super(pos, player);

        this.stateType = stateType;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);

        buf.writeInt(stateType.id);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);

        stateType = EnumStateType.byId(buf.readInt());
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
                            State state = te.getState(message.stateType);

                            if (state != null)
                                message.respond(world, new StateUpdatePacketToClient(message.pos, message.stateType, state));
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
