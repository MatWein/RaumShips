package com.mw.raumships.client.network;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.gui.rings.EnumStateType;
import com.mw.raumships.client.gui.rings.ITileEntityStateProvider;
import com.mw.raumships.client.gui.rings.State;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StateUpdatePacketToClient extends PositionedPacket {
	public StateUpdatePacketToClient() {}
	
	private EnumStateType stateType;
	private State state;
	
	private ByteBuf stateBuf;
	
	public StateUpdatePacketToClient(BlockPos pos, EnumStateType stateType, State state) {
		super(pos);
		
		this.stateType = stateType;
		this.state = state;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {		
		super.toBytes(buf);
		
		buf.writeInt(stateType.id);
		
		state.toBytes(buf);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);

		stateType = EnumStateType.byId(buf.readInt());
		stateBuf = buf.copy();
	}
	
	public static class StateUpdateClientHandler implements IMessageHandler<StateUpdatePacketToClient, IMessage> {

		@Override
		public IMessage onMessage(StateUpdatePacketToClient message, MessageContext ctx) {			
			EntityPlayer player = RaumShipsMod.proxy.getPlayerClientSide();
			World world = player.getEntityWorld();

			RaumShipsMod.proxy.addScheduledTaskClientSide(() -> {
								
				ITileEntityStateProvider te = (ITileEntityStateProvider) world.getTileEntity(message.pos);
				
				try {
					State state = te.createState(message.stateType);
					state.fromBytes(message.stateBuf);
					
					if (te != null && state != null)
						te.setState(message.stateType, state);
				}
				
				catch (UnsupportedOperationException e) {
					e.printStackTrace();
				}
			});
			
			return null;
		}
		
	}
}
