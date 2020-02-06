package com.mw.raumships.server.network;

import com.mw.raumships.client.network.PositionedPlayerPacket;
import com.mw.raumships.client.network.StateUpdatePacketToClient;
import com.mw.raumships.common.blocks.rings.RingsTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.StandardCharsets;

public class SaveRingsParametersToServer extends PositionedPlayerPacket {
    int address;
    String name;

    public SaveRingsParametersToServer() {
    }

    public SaveRingsParametersToServer(BlockPos pos, EntityPlayer player, int address, String name) {
        super(pos, player);

        this.address = address;
        this.name = name;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);

        buf.writeInt(address);
        buf.writeInt(name.length());
        buf.writeCharSequence(name, StandardCharsets.UTF_8);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);

        address = buf.readInt();
        int len = buf.readInt();
        name = buf.readCharSequence(len, StandardCharsets.UTF_8).toString();
    }

    public static class SaveRingsParametersServerHandler implements IMessageHandler<SaveRingsParametersToServer, IMessage> {
        @Override
        public StateUpdatePacketToClient onMessage(SaveRingsParametersToServer message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            WorldServer world = player.getServerWorld();

            world.addScheduledTask(() -> {
                RingsTile ringsTile = (RingsTile) world.getTileEntity(message.pos);
                ringsTile.setRingsParams(player, message.address, message.name);

                message.respond(world, new StateUpdatePacketToClient(message.pos, ringsTile.getState()));
            });

            return null;
        }
    }
}
