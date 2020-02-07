package com.mw.raumships.client.network;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.gui.rings.RingsGuiState;
import com.mw.raumships.common.blocks.rings.RingsTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StateUpdatePacketToClient extends PositionedPacket {
    private RingsGuiState state;
    private boolean shouldOpenGui;

    public StateUpdatePacketToClient() {
    }

    public StateUpdatePacketToClient(BlockPos pos, RingsGuiState state, boolean shouldOpenGui) {
        super(pos);

        this.state = state;
        this.shouldOpenGui = shouldOpenGui;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);

        buf.writeBoolean(shouldOpenGui);

        state.toBytes(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);

        shouldOpenGui = buf.readBoolean();

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

                    if (message.shouldOpenGui) {
                        Minecraft.getMinecraft().displayGuiScreen(te.openGui);
                    }
                }
            });

            return null;
        }
    }
}
