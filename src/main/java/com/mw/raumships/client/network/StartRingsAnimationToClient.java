package com.mw.raumships.client.network;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.sound.Sounds;
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
	public BlockPos targetRingsPos;
	
	public StartRingsAnimationToClient(BlockPos pos, long animationStart, BlockPos targetRingsPos) {
		super(pos);
		
		this.animationStart = animationStart;
		this.targetRingsPos = targetRingsPos;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		
		buf.writeLong(animationStart);
		buf.writeLong(targetRingsPos.toLong());
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		
		animationStart = buf.readLong();
		targetRingsPos = BlockPos.fromLong(buf.readLong());
	}
	
	public static class StartRingsAnimationToClientHandler implements IMessageHandler<StartRingsAnimationToClient, IMessage> {
		@Override
		public IMessage onMessage(StartRingsAnimationToClient message, MessageContext ctx) {
			EntityPlayer player = RaumShipsMod.proxy.getPlayerClientSide();
			World world = player.getEntityWorld();

			RaumShipsMod.proxy.addScheduledTaskClientSide(() -> {
				RingsTile ringsTile = (RingsTile) world.getTileEntity(message.pos);
			
				if (ringsTile != null) {
					Sounds.playSound(message.pos, Sounds.RINGS_TRANSPORT, 0.8f);
					Sounds.playSound(message.targetRingsPos, Sounds.RINGS_TRANSPORT, 0.8f);

					ringsTile.getTransportRingsRenderer().animationStart(message.animationStart);
				}
			});
						
			return null;
		}
	}
}
