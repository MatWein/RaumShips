package com.mw.raumships.server.network;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.network.PositionedPacket;
import com.mw.raumships.client.network.RingsControllerActivatedToClient;
import com.mw.raumships.common.blocks.rings.RingsControllerTile;
import com.mw.raumships.common.blocks.rings.RingsTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RingsControllerActivatedToServer extends PositionedPacket {
    public int address;

    public RingsControllerActivatedToServer() {
    }

    public RingsControllerActivatedToServer(BlockPos pos, int address) {
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

    public static class RingsControllerActivatedServerHandler implements IMessageHandler<RingsControllerActivatedToServer, IMessage> {
        @Override
        public IMessage onMessage(RingsControllerActivatedToServer message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            WorldServer world = player.getServerWorld();

            world.addScheduledTask(() -> {
                RingsTile ringsTile = getRingsTile(world, message.pos);

                if (ringsTile != null) {
                    ringsTile.attemptTransportTo(player, message.address);
                    RaumShipsMod.proxy.getNetworkWrapper().sendTo(new RingsControllerActivatedToClient(message.pos, message.address), player);
                } else {
                    player.sendStatusMessage(new TextComponentTranslation("tile.rings_controller_block.not_linked"), true);
                }
            });

            return null;
        }

        private RingsTile getRingsTile(WorldServer world, BlockPos pos) {
            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity instanceof RingsControllerTile) {
                return ((RingsControllerTile)tileEntity).getLinkedRingsTile(world);
            } else if (tileEntity instanceof RingsTile) {
                return (RingsTile)tileEntity;
            } else {
                return null;
            }
        }
    }

}
