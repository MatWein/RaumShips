package com.mw.raumships.client.network;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.sound.Sounds;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TRControllerActivatedToClient extends PositionedPacket {
    public int address;

    public TRControllerActivatedToClient() {
    }

    public TRControllerActivatedToClient(BlockPos pos, int address) {
        super(pos);

        this.address = address;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);

        buf.writeInt(address);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);

        address = buf.readInt();
    }

    public static class TRControllerActivatedToClientHandler implements IMessageHandler<TRControllerActivatedToClient, IMessage> {
        @Override
        public IMessage onMessage(TRControllerActivatedToClient message, MessageContext ctx) {
            RaumShipsMod.proxy.addScheduledTaskClientSide(() -> {
                Sounds.playSound(message.pos, Sounds.RINGS_CONTROLLER_BUTTON, 0.5F);
            });

            return null;
        }
    }
}
