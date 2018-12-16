package ftblag.stonechest.tileentities;

import ftblag.stonechest.blocks.BlockStoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.VanillaDoubleChestItemHandler;

import javax.annotation.Nullable;

public class TileEntityStoneChest extends TileEntityChest {

    private EnumStoneChest type;

    public TileEntityStoneChest() {
    }

    public TileEntityStoneChest(EnumStoneChest typeIn) {
        this.type = typeIn;
    }

    // Copy from VanillaDoubleChestItemHandler
    @Nullable
    public static VanillaDoubleChestItemHandler getDoubleChestHandler(TileEntityStoneChest chest) {
        World world = chest.getWorld();
        BlockPos pos = chest.getPos();
        if (world == null || pos == null || !world.isBlockLoaded(pos))
            return null; // Still loading

        Block blockType = chest.getBlockType();

        EnumFacing[] horizontals = EnumFacing.HORIZONTALS;
        for (int i = horizontals.length - 1; i >= 0; i--) { // Use reverse order so we can return early
            EnumFacing enumfacing = horizontals[i];
            BlockPos blockpos = pos.offset(enumfacing);
            Block block = world.getBlockState(blockpos).getBlock();

            if (block == blockType) {
                TileEntity otherTE = world.getTileEntity(blockpos);

                if (otherTE instanceof TileEntityStoneChest) {
                    TileEntityStoneChest otherChest = (TileEntityStoneChest) otherTE;
                    if (otherChest.getPlacedType().equals(chest.getPlacedType()))
                        return new VanillaDoubleChestItemHandler(chest, otherChest, enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH);
                }
            }
        }
        return VanillaDoubleChestItemHandler.NO_ADJACENT_CHESTS_INSTANCE; // All alone
    }

    @SuppressWarnings("incomplete-switch")
    private void setNeighbor(TileEntityChest chestTe, EnumFacing side) {
        if (chestTe.isInvalid()) {
            this.adjacentChestChecked = false;
        } else if (this.adjacentChestChecked) {
            switch (side) {
                case NORTH:

                    if (this.adjacentChestZNeg != chestTe) {
                        this.adjacentChestChecked = false;
                    }

                    break;
                case SOUTH:

                    if (this.adjacentChestZPos != chestTe) {
                        this.adjacentChestChecked = false;
                    }

                    break;
                case EAST:

                    if (this.adjacentChestXPos != chestTe) {
                        this.adjacentChestChecked = false;
                    }

                    break;
                case WEST:

                    if (this.adjacentChestXNeg != chestTe) {
                        this.adjacentChestChecked = false;
                    }
            }
        }
    }

    @Nullable
    @Override
    protected TileEntityChest getAdjacentChest(EnumFacing side) {
        BlockPos blockpos = this.pos.offset(side);

        if (this.isChestAt(blockpos)) {
            TileEntity tileentity = this.world.getTileEntity(blockpos);

            if (tileentity instanceof TileEntityStoneChest) {
                TileEntityStoneChest tileentitystonechest = (TileEntityStoneChest) tileentity;
                tileentitystonechest.setNeighbor(this, side.getOpposite());
                return tileentitystonechest;
            }
        }

        return null;
    }

    private boolean isChestAt(BlockPos posIn) {
        if (this.world == null) {
            return false;
        } else {
            Block block = this.world.getBlockState(posIn).getBlock();
            return block instanceof BlockStoneChest && ((BlockStoneChest) block).type == getPlacedType();
        }
    }

    public EnumStoneChest getPlacedType() {
        if (this.type == null) {
            if (this.world == null || !(this.getBlockType() instanceof BlockStoneChest)) {
                return EnumStoneChest.COBBLESTONE;
            }
            this.type = ((BlockStoneChest) this.getBlockType()).type;
        }
        return this.type;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.add(-1, 0, -1), pos.add(2, 2, 2));
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (doubleChestHandler == null || doubleChestHandler.needsRefresh())
                doubleChestHandler = getDoubleChestHandler(this);
            if (doubleChestHandler != null && doubleChestHandler != VanillaDoubleChestItemHandler.NO_ADJACENT_CHESTS_INSTANCE)
                return (T) doubleChestHandler;
        }
        return super.getCapability(capability, facing);
    }
}
