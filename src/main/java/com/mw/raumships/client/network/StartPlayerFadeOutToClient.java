package com.mw.raumships.client.network;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.gui.event.PlayerFadeOutRenderEvent;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StartPlayerFadeOutToClient implements IMessage {
    public StartPlayerFadeOutToClient() {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    public static class StartPlayerFadeOutToClientHandler implements IMessageHandler<StartPlayerFadeOutToClient, IMessage> {
        @Override
        public IMessage onMessage(StartPlayerFadeOutToClient message, MessageContext ctx) {
            RaumShipsMod.proxy.addScheduledTaskClientSide(PlayerFadeOutRenderEvent::startFadeOut);

            return null;
        }
    }
}
