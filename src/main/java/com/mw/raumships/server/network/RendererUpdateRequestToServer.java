package com.mw.raumships.server.network;

import com.mw.raumships.client.network.PositionedPlayerPacket;
import com.mw.raumships.client.network.RendererUpdatePacketToClient;
import com.mw.raumships.client.rendering.rings.ITileEntityRendered;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RendererUpdateRequestToServer extends PositionedPlayerPacket {
	public RendererUpdateRequestToServer() {}	
		
	public RendererUpdateRequestToServer(BlockPos pos, EntityPlayer player) {
		super(pos, player);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
	}	
	
	public static class TileUpdateServerHandler implements IMessageHandler<RendererUpdateRequestToServer, IMessage> {

		@Override
		public RendererUpdatePacketToClient onMessage(RendererUpdateRequestToServer message, MessageContext ctx) {
						
			WorldServer world = ctx.getServerHandler().player.getServerWorld();
			
			if (world.isBlockLoaded(message.pos)) {
				world.addScheduledTask(() -> {
					TileEntity te = world.getTileEntity(message.pos);
					
					if (te != null) {													
						message.respond(world, new RendererUpdatePacketToClient(message.pos, ((ITileEntityRendered) te).getRendererState()));
					}
				});
			}
			
			return null;
		}
		
	}
}
