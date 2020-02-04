package com.mw.raumships.client.network;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.common.blocks.rings.RingsTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StartRingsAnimationToClient extends PositionedPacket {
	public StartRingsAnimationToClient() {}
	
	public long animationStart;
	
	public StartRingsAnimationToClient(BlockPos pos, long animationStart) {
		super(pos);
		
		this.animationStart = animationStart;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		
		buf.writeLong(animationStart);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		
		animationStart = buf.readLong();
	}
	
	public static class StartRingsAnimationToClientHandler implements IMessageHandler<StartRingsAnimationToClient, IMessage> {

		@Override
		public IMessage onMessage(StartRingsAnimationToClient message, MessageContext ctx) {
			EntityPlayer player = RaumShipsMod.proxy.getPlayerClientSide();
			World world = player.getEntityWorld();

			RaumShipsMod.proxy.addScheduledTaskClientSide(() -> {
				RingsTile ringsTile = (RingsTile) world.getTileEntity(message.pos);
			
				if (ringsTile != null) {
					ringsTile.getTransportRingsRenderer().animationStart(message.animationStart);
				}	
			});
						
			return null;
		}
		
	}
}
