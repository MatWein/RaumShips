package com.mw.raumships.server.network;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.network.PositionedPacket;
import com.mw.raumships.client.network.TRControllerActivatedToClient;
import com.mw.raumships.common.blocks.rings.RingsControllerTile;
import com.mw.raumships.common.blocks.rings.RingsTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TRControllerActivatedToServer extends PositionedPacket {
    public int address;

    public TRControllerActivatedToServer() {
    }

    public TRControllerActivatedToServer(BlockPos pos, int address) {
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

    public static class TRControllerActivatedServerHandler implements IMessageHandler<TRControllerActivatedToServer, IMessage> {
        @Override
        public IMessage onMessage(TRControllerActivatedToServer message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            WorldServer world = player.getServerWorld();

            world.addScheduledTask(() -> {
                RingsControllerTile controllerTile = (RingsControllerTile) world.getTileEntity(message.pos);
                RingsTile ringsTile = controllerTile.getLinkedRingsTile(world);

                if (ringsTile != null) {
                    ringsTile.attemptTransportTo(player, message.address);
                    RaumShipsMod.proxy.getNetworkWrapper().sendTo(new TRControllerActivatedToClient(message.pos, message.address), player);
                } else {
                    player.sendStatusMessage(new TextComponentTranslation("tile.aunis.transportrings_controller_block.not_linked"), true);
                }
            });

            return null;
        }
    }
}
