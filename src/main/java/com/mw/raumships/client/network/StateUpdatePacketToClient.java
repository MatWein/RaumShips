package com.mw.raumships.client.network;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.gui.rings.RingsGuiState;
import com.mw.raumships.common.blocks.rings.DtoRingsModel;
import com.mw.raumships.common.blocks.rings.RingsTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.StandardCharsets;

public class StateUpdatePacketToClient extends PositionedPacket {
    private RingsGuiState state;
    private DtoRingsModel ringsModel;
    private boolean shouldOpenGui;

    public StateUpdatePacketToClient() {
    }

    public StateUpdatePacketToClient(BlockPos pos, RingsGuiState state, DtoRingsModel ringsModel, boolean shouldOpenGui) {
        super(pos);

        this.state = state;
        this.ringsModel = ringsModel;
        this.shouldOpenGui = shouldOpenGui;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);

        buf.writeBoolean(shouldOpenGui);
        buf.writeInt(ringsModel.getAddress());
        buf.writeInt(ringsModel.getName().length());
        buf.writeCharSequence(ringsModel.getName(), StandardCharsets.UTF_8);
        buf.writeLong(ringsModel.getPos().toLong());
        buf.writeDouble(ringsModel.getDistance());
        buf.writeBoolean(ringsModel.isClone());

        state.toBytes(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);

        shouldOpenGui = buf.readBoolean();

        ringsModel = new DtoRingsModel();
        ringsModel.setAddress(buf.readInt());

        int nameLength = buf.readInt();
        ringsModel.setName(buf.readCharSequence(nameLength, StandardCharsets.UTF_8).toString());
        ringsModel.setPos(BlockPos.fromLong(buf.readLong()));
        ringsModel.setDistance(buf.readDouble());
        ringsModel.setClone(buf.readBoolean());

        state = new RingsGuiState();
        state.fromBytes(buf);
    }

    public static class StateUpdateClientHandler implements IMessageHandler<StateUpdatePacketToClient, IMessage> {
        @Override
        public IMessage onMessage(StateUpdatePacketToClient message, MessageContext ctx) {
            EntityPlayer player = RaumShipsMod.proxy.getPlayerClientSide();
            World world = player.getEntityWorld();

            RaumShipsMod.proxy.addScheduledTaskClientSide(() -> {
                RingsTile te = (RingsTile) world.getTileEntity(message.pos);

                if (te != null) {
                    te.setState(message.state);
                    te.setRings(message.ringsModel);

                    if (message.shouldOpenGui) {
                        te.showMenu();
                    }
                }
            });

            return null;
        }
    }
}
