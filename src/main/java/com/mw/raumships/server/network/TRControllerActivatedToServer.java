package com.mw.raumships.server.network;

import com.mw.raumships.client.network.PositionedPacket;
import com.mw.raumships.client.sound.Sounds;
import com.mw.raumships.common.blocks.rings.RingsControllerTile;
import com.mw.raumships.common.blocks.rings.RingsTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
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
                    Sounds.playSoundEvent(message.pos, Sounds.RINGS_CONTROLLER_BUTTON, 0.5F);

                    ringsTile.attemptTransportTo(player, message.address);
                } else
                    player.sendStatusMessage(new TextComponentString(I18n.format("tile.rings_controller_block.not_linked")), true);
            });

            return null;
        }

    }
}
