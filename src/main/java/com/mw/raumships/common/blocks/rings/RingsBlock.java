package com.mw.raumships.common.blocks.rings;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.client.gui.rings.LinkingHelper;
import com.mw.raumships.client.network.StateUpdatePacketToClient;
import com.mw.raumships.common.items.AnalyzerAncientItem;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class RingsBlock extends Block {
    private static final String REGISTRY_NAME = "rings_block";

    public RingsBlock() {
        super(Material.IRON);

        setUnlocalizedName(REGISTRY_NAME);
        setRegistryName(RaumShipsMod.MODID, REGISTRY_NAME);
        setSoundType(SoundType.STONE);

        setLightOpacity(0);

        setHardness(3.0f);
        setHarvestLevel("pickaxe", 3);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        RingsTile ringsTile = (RingsTile) world.getTileEntity(pos);

        if (!world.isRemote) {
            player.sendStatusMessage(new TextComponentTranslation("tile.rings_block.energyStored", ringsTile.getEnergyStored(), ringsTile.getMaxEnergyStored()), true);

            if (player.getHeldItem(hand).getItem() instanceof AnalyzerAncientItem)
                RaumShipsMod.proxy.getNetworkWrapper().sendTo(new StateUpdatePacketToClient(pos, ringsTile.getState(), ringsTile.getRings(), true), (EntityPlayerMP) player);
        }

        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        RingsTile ringsTile = (RingsTile) world.getTileEntity(pos);

        BlockPos closestController = LinkingHelper.findClosestUnlinked(world, pos, new BlockPos(10, 5, 10), RingsControllerBlock.class);

        if (closestController != null) {
            RingsControllerTile controllerTile = (RingsControllerTile) world.getTileEntity(closestController);

            controllerTile.setLinkedRings(pos);
            ringsTile.setLinkedController(closestController);
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        RingsTile ringsTile = (RingsTile) world.getTileEntity(pos);

        if (ringsTile.isLinked())
            ringsTile.getLinkedControllerTile(world).setLinkedRings(null);

        ringsTile.removeAllRings();
        ringsTile.setBarrierBlocks(false);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public RingsTile createTileEntity(World world, IBlockState state) {
        return new RingsTile();
    }
}
