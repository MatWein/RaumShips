package com.mw.raumships.common.blocks.rings;

import com.mw.raumships.RaumShipsConfig;
import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.gui.rings.*;
import com.mw.raumships.client.network.StartPlayerFadeOutToClient;
import com.mw.raumships.client.network.StartRingsAnimationToClient;
import com.mw.raumships.client.rendering.rings.*;
import com.mw.raumships.server.network.RendererUpdateRequestToServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class RingsTile extends TileEntity implements ITileEntityRendered, ITickable, ILinkable {
    private boolean firstTick = true;
    private boolean waitForStart = false;
    private boolean waitForFadeOut = false;
    private boolean waitForTeleport = false;
    private boolean waitForClearout = false;

    private long buttonPressed;

    private static final int fadeOutTimeout = (int) (30 + RingsRenderer.uprisingInterval * RingsRenderer.ringCount + RingsRenderer.animationDiv * Math.PI);
    public static final int fadeOutTotalTime = 2 * 20; // 2s

    private static final int teleportTimeout = fadeOutTimeout + fadeOutTotalTime / 2;
    private static final int clearoutTimeout = (int) (100 + RingsRenderer.fallingInterval * RingsRenderer.ringCount + RingsRenderer.animationDiv * Math.PI);

    private List<Entity> teleportList;

    @Override
    public void update() {
        if (firstTick) {
            firstTick = false;

            if (world.isRemote) {
                RaumShipsMod.proxy.getNetworkWrapper().sendToServer(new RendererUpdateRequestToServer(pos, RaumShipsMod.proxy.getPlayerClientSide()));
            }
        }

        if (!world.isRemote) {
            long effTick = world.getTotalWorldTime();

            effTick -= waitForStart ? buttonPressed : getTransportRingsRendererState().animationStart;

            if (waitForStart && effTick >= 20) {
                waitForStart = false;
                waitForFadeOut = true;

                animationStart();
                setBarrierBlocks(true);
            } else if (waitForFadeOut && effTick >= fadeOutTimeout) {
                waitForFadeOut = false;
                waitForTeleport = true;

                teleportList = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.add(-2, 2, -2), pos.add(3, 6, 3)));

                for (Entity entity : teleportList) {
                    if (entity instanceof EntityPlayerMP) {
                        RaumShipsMod.proxy.getNetworkWrapper().sendTo(new StartPlayerFadeOutToClient(), (EntityPlayerMP) entity);
                    }
                }
            } else if (waitForTeleport && effTick >= teleportTimeout) {
                waitForTeleport = false;
                waitForClearout = true;

                BlockPos teleportVector = targetRingsPos.subtract(pos);

                for (Entity entity : teleportList) {
                    if (!excludedEntities.contains(entity)) {
                        BlockPos ePos = entity.getPosition().add(teleportVector);

                        entity.setPositionAndUpdate(ePos.getX(), ePos.getY(), ePos.getZ());
                    }
                }
            } else if (waitForClearout && effTick >= clearoutTimeout) {
                waitForClearout = false;

                setBarrierBlocks(false);
            }
        }
    }

    private BlockPos targetRingsPos;
    private List<Entity> excludedEntities;

    public List<Entity> startAnimationAndTeleport(BlockPos targetRingsPos, List<Entity> excludedEntities) {
        this.targetRingsPos = targetRingsPos;
        this.excludedEntities = excludedEntities;

        waitForStart = true;
        buttonPressed = world.getTotalWorldTime();

        return world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.add(-2, 2, -2), pos.add(3, 6, 3)));
    }

    public void animationStart() {
        getTransportRingsRendererState().animationStart = world.getTotalWorldTime();
        getTransportRingsRendererState().ringsUprising = true;
        getTransportRingsRendererState().isAnimationActive = true;

        TargetPoint point = new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 512);
        RaumShipsMod.proxy.getNetworkWrapper().sendToAllTracking(new StartRingsAnimationToClient(
                pos,
                getTransportRingsRendererState().animationStart,
                targetRingsPos), point);
    }

    public void attemptTransportTo(EntityPlayer player, int address) {
        if (checkIfObstructed(this.pos)) {
            player.sendStatusMessage(new TextComponentTranslation("tile.rings_block.obstructed"), true);
            return;
        }

        DtoRingsModel rings = ringsMap.get(address);

        if (rings != null) {
            BlockPos targetRingsPos = rings.getPos();
            if (checkIfObstructed(targetRingsPos)) {
                player.sendStatusMessage(new TextComponentTranslation("tile.rings_block.targetObstructed"), true);
                return;
            }

            List<Entity> excludedFromReceivingSite = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.add(-2, 2, -2), pos.add(3, 6, 3)));

            RingsTile targetRingsTile = (RingsTile) world.getTileEntity(targetRingsPos);

            List<Entity> excludedEntities = targetRingsTile.startAnimationAndTeleport(pos, excludedFromReceivingSite);
            startAnimationAndTeleport(targetRingsPos, excludedEntities);
        } else {
            player.sendStatusMessage(new TextComponentTranslation("tile.rings_block.non_existing_address"), true);
        }
    }

    private static final List<BlockPos> invisibleBlocksTemplate = Arrays.asList(
            new BlockPos(0, 2, 3),
            new BlockPos(1, 2, 3),
            new BlockPos(2, 2, 2),
            new BlockPos(3, 2, 1)
    );

    private boolean checkIfObstructed(BlockPos pos) {
        if (RaumShipsConfig.ringsConfig.ignoreObstructionCheck)
            return false;

        for (int y = 0; y < 4; y++) {
            for (Rotation rotation : Rotation.values()) {
                for (BlockPos invPos : invisibleBlocksTemplate) {

                    BlockPos newPos = new BlockPos(pos).add(invPos.rotate(rotation)).add(0, y, 0);

                    Block block = world.getBlockState(newPos).getBlock();

                    if (block != Blocks.AIR && !block.isReplaceable(world, newPos)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private List<BlockPos> invisibleBlocks = new ArrayList<>();

    private void setBarrierBlocks(boolean set) {
        if (set) {
            invisibleBlocks.clear();

            for (int y = 1; y < 4; y++) {
                for (Rotation rotation : Rotation.values()) {
                    for (BlockPos invPos : invisibleBlocksTemplate) {

                        BlockPos newPos = new BlockPos(this.pos).add(invPos.rotate(rotation)).add(0, y, 0);
                        IBlockState state = world.getBlockState(newPos);

                        if (state.getBlock() != Blocks.AIR) {
                            state.getBlock().dropBlockAsItem(world, newPos, state, 0);
                        }

                        world.setBlockState(newPos, RaumShipsMod.proxy.getInvisibleBlock().getDefaultState(), 3);

                        invisibleBlocks.add(newPos);
                    }
                }
            }
        } else {
            for (BlockPos invPos : invisibleBlocks) {
                world.setBlockToAir(invPos);
            }
        }
    }

    private BlockPos linkedController;

    public void setLinkedController(BlockPos pos) {
        this.linkedController = pos;

        markDirty();
    }

    @Override
    public boolean isLinked() {
        return linkedController != null;
    }

    public RingsControllerTile getLinkedControllerTile(World world) {
        return (linkedController != null ? ((RingsControllerTile) world.getTileEntity(linkedController)) : null);
    }

    private DtoRingsModel rings;

    private DtoRingsModel getRings() {
        if (rings == null)
            rings = new DtoRingsModel(pos);

        return rings;
    }

    public DtoRingsModel getClonedRings(BlockPos callerPos) {
        return getRings().cloneWithNewDistance(callerPos);
    }

    public Map<Integer, DtoRingsModel> ringsMap = new HashMap<>();

    public void addRings(RingsTile caller) {
        DtoRingsModel clonedRings = caller.getClonedRings(this.pos);

        if (clonedRings.isInGrid()) {
            ringsMap.put(clonedRings.getAddress(), clonedRings);

            markDirty();
        }
    }

    public void removeRings(int address) {
        if (ringsMap.remove(address) != null)
            markDirty();
    }

    public void removeAllRings() {
        for (DtoRingsModel rings : ringsMap.values()) {

            RingsTile ringsTile = (RingsTile) world.getTileEntity(rings.getPos());
            ringsTile.removeRings(getRings().getAddress());
        }
    }

    public void setRingsParams(EntityPlayer player, int address, String name) {
        int x = pos.getX();
        int z = pos.getZ();

        int radius = RaumShipsConfig.ringsConfig.rangeFlat;

        int y = pos.getY();
        int vertical = RaumShipsConfig.ringsConfig.rangeVertical;

        List<RingsTile> ringsTilesInRange = new ArrayList<>();

        for (BlockPos newRingsPos : BlockPos.getAllInBoxMutable(new BlockPos(x - radius, y - vertical, z - radius), new BlockPos(x + radius, y + vertical, z + radius))) {
            if (world.getBlockState(newRingsPos).getBlock() instanceof RingsBlock && !pos.equals(newRingsPos)) {

                RingsTile newRingsTile = (RingsTile) world.getTileEntity(newRingsPos);
                ringsTilesInRange.add(newRingsTile);

                int newRingsAddress = newRingsTile.getClonedRings(pos).getAddress();
                if (newRingsAddress == address && newRingsAddress != -1) {
                    player.sendStatusMessage(new TextComponentTranslation("tile.rings_block.duplicate_address"), true);
                    return;
                }
            }
        }

        removeAllRings();

        getRings().setAddress(address);
        getRings().setName(name);

        for (RingsTile newRingsTile : ringsTilesInRange) {
            this.addRings(newRingsTile);
            newRingsTile.addRings(this);
        }

        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        getRendererState().toNBT(compound);

        compound.setTag("ringsData", getRings().serializeNBT());
        if (linkedController != null)
            compound.setLong("linkedController", linkedController.toLong());

        compound.setInteger("ringsMapLength", ringsMap.size());

        int i = 0;
        for (DtoRingsModel rings : ringsMap.values()) {
            compound.setTag("ringsMap" + i, rings.serializeNBT());

            i++;
        }

        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        getRendererState().fromNBT(compound);

        if (compound.hasKey("ringsData"))
            getRings().deserializeNBT((NBTTagCompound) compound.getTag("ringsData"));

        if (compound.hasKey("linkedController"))
            linkedController = BlockPos.fromLong(compound.getLong("linkedController"));

        if (compound.hasKey("ringsMapLength")) {
            int len = compound.getInteger("ringsMapLength");

            ringsMap.clear();

            for (int i = 0; i < len; i++) {
                DtoRingsModel rings = new DtoRingsModel(null).deserializeNBT((NBTTagCompound) compound.getTag("ringsMap" + i));

                ringsMap.put(rings.getAddress(), rings);
            }
        }

        super.readFromNBT(compound);
    }

    public State getState(EnumStateType stateType) {
        switch (stateType) {
            case GUI_STATE:
                return new RingsGuiState(getRings(), ringsMap.values());

            default:
                return null;
        }
    }

    public State createState(EnumStateType stateType) {
        switch (stateType) {
            case GUI_STATE:
                return new RingsGuiState();

            default:
                return null;
        }
    }

    @SideOnly(Side.CLIENT)
    private RingsGUI openGui;

    @SideOnly(Side.CLIENT)
    public void setState(EnumStateType stateType, State state) {
        switch (stateType) {
            case GUI_STATE:
                if (openGui == null) {
                    openGui = new RingsGUI(pos, (RingsGuiState) state);
                    Minecraft.getMinecraft().displayGuiScreen(openGui);
                } else if (!openGui.isOpen) {
                    Minecraft.getMinecraft().displayGuiScreen(openGui);
                    openGui.isOpen = true;
                } else {
                    openGui.state = (RingsGuiState) state;
                }
                break;
            default:
                break;
        }
    }

    RingsRenderer renderer;
    RingsRendererState rendererState;

    @Override
    public ISpecialRenderer<RingsRendererState> getRenderer() {
        if (renderer == null)
            renderer = new RingsRenderer(this);

        return renderer;
    }

    public RingsRenderer getTransportRingsRenderer() {
        return (RingsRenderer) getRenderer();
    }

    @Override
    public RendererState getRendererState() {
        if (rendererState == null)
            rendererState = new RingsRendererState();

        return rendererState;
    }

    public RingsRendererState getTransportRingsRendererState() {
        return (RingsRendererState) getRendererState();
    }

    @Override
    public RendererState createRendererState(ByteBuf buf) {
        return new RingsRendererState().fromBytes(buf);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.add(-4, 0, -4), pos.add(4, 7, 4));
    }
}
