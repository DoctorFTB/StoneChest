package ftblag.stonechest.blocks;

import ftblag.stonechest.StoneChest;
import ftblag.stonechest.tileentities.TileEntityStoneChest;
import net.minecraft.block.BlockChest;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

import javax.annotation.Nullable;

public class BlockStoneChest extends BlockChest {

    public static final Type TYPE_STONE = EnumHelper.addEnum(Type.class, "STONE", new Class[0]);

    public EnumStoneChest type;

    public BlockStoneChest(EnumStoneChest type, String name) {
        super(TYPE_STONE);
        this.type = type;
        setTranslationKey(name);
        setRegistryName(StoneChest.MODID, name);
        setHardness(2.5F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return isType(type, source, pos.north()) ? NORTH_CHEST_AABB : isType(type, source, pos.south()) ? SOUTH_CHEST_AABB : isType(type, source, pos.west()) ? WEST_CHEST_AABB : isType(type, source, pos.east()) ? EAST_CHEST_AABB : NOT_CONNECTED_AABB;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.checkForSurroundingChests(worldIn, pos, state);

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            BlockPos blockpos = pos.offset(enumfacing);
            IBlockState iblockstate = worldIn.getBlockState(blockpos);

            if (iblockstate.getBlock() == this) {
                this.checkForSurroundingChests(worldIn, blockpos, iblockstate);
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing enumfacing = EnumFacing.byHorizontalIndex(MathHelper.floor((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
        state = state.withProperty(FACING, enumfacing);
        BlockPos northBP = pos.north();
        BlockPos southBP = pos.south();
        BlockPos westBP = pos.west();
        BlockPos eastBP = pos.east();
        boolean flagN = isType(type, worldIn, northBP);
        boolean flagS = isType(type, worldIn, southBP);
        boolean flagW = isType(type, worldIn, westBP);
        boolean flagE = isType(type, worldIn, eastBP);

        if (!flagN && !flagS && !flagW && !flagE) {
            worldIn.setBlockState(pos, state, 3);
        } else if (enumfacing.getAxis() != EnumFacing.Axis.X || !flagN && !flagS) {
            if (enumfacing.getAxis() == EnumFacing.Axis.Z && (flagW || flagE)) {
                if (flagW) {
                    setBlockState(worldIn, westBP, state, 3);
                } else {
                    setBlockState(worldIn, eastBP, state, 3);
                }

                worldIn.setBlockState(pos, state, 3);
            }
        } else {
            if (flagN) {
                setBlockState(worldIn, northBP, state, 3);
            } else {
                setBlockState(worldIn, southBP, state, 3);
            }

            worldIn.setBlockState(pos, state, 3);
        }

        if (stack.hasDisplayName()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityStoneChest) {
                ((TileEntityStoneChest) tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }

    private void setBlockState(World world, BlockPos pos, IBlockState state, int flag) {
        TileEntity te = world.getTileEntity(pos);
        world.setBlockState(pos, state, flag);
        if (te != null) {
            te.validate();
            world.setTileEntity(pos, te);
            if(te instanceof TileEntityStoneChest)
                ((TileEntityStoneChest) te).adjacentChestChecked = false;
        }
    }

    public IBlockState checkForSurroundingChests(World worldIn, BlockPos pos, IBlockState state) {
        if (worldIn.isRemote) {
            return state;
        } else {
            boolean northSame = isType(type, worldIn, pos.north());
            boolean southSame = isType(type, worldIn, pos.south());
            boolean westSame = isType(type, worldIn, pos.west());
            boolean eastSame = isType(type, worldIn, pos.east());
            IBlockState iblockstate = worldIn.getBlockState(pos.north());
            IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
            IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
            IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
            EnumFacing enumfacing = state.getValue(FACING);

            if (!northSame && !southSame) {
                boolean flag = iblockstate.isFullBlock();
                boolean flag1 = iblockstate1.isFullBlock();

                if (westSame || eastSame) {
                    BlockPos blockpos1 = westSame ? pos.west() : pos.east();
                    IBlockState iblockstate7 = worldIn.getBlockState(blockpos1.north());
                    IBlockState iblockstate6 = worldIn.getBlockState(blockpos1.south());
                    enumfacing = EnumFacing.SOUTH;
                    EnumFacing enumfacing2;

                    if (westSame) {
                        enumfacing2 = iblockstate2.getValue(FACING);
                    } else {
                        enumfacing2 = iblockstate3.getValue(FACING);
                    }

                    if (enumfacing2 == EnumFacing.NORTH) {
                        enumfacing = EnumFacing.NORTH;
                    }

                    if ((flag || iblockstate7.isFullBlock()) && !flag1 && !iblockstate6.isFullBlock()) {
                        enumfacing = EnumFacing.SOUTH;
                    }

                    if ((flag1 || iblockstate6.isFullBlock()) && !flag && !iblockstate7.isFullBlock()) {
                        enumfacing = EnumFacing.NORTH;
                    }
                }
            } else {
                BlockPos blockpos = northSame ? pos.north() : pos.south();
                IBlockState iblockstate4 = worldIn.getBlockState(blockpos.west());
                IBlockState iblockstate5 = worldIn.getBlockState(blockpos.east());
                enumfacing = EnumFacing.EAST;
                EnumFacing enumfacing1;

                if (northSame) {
                    enumfacing1 = iblockstate.getValue(FACING);
                } else {
                    enumfacing1 = iblockstate1.getValue(FACING);
                }

                if (enumfacing1 == EnumFacing.WEST) {
                    enumfacing = EnumFacing.WEST;
                }

                if ((iblockstate2.isFullBlock() || iblockstate4.isFullBlock()) && !iblockstate3.isFullBlock() && !iblockstate5.isFullBlock()) {
                    enumfacing = EnumFacing.EAST;
                }

                if ((iblockstate3.isFullBlock() || iblockstate5.isFullBlock()) && !iblockstate2.isFullBlock() && !iblockstate4.isFullBlock()) {
                    enumfacing = EnumFacing.WEST;
                }
            }

            state = state.withProperty(FACING, enumfacing);
            setBlockState(worldIn, pos, state, 3);
            return state;
        }
    }

    @Override
    public IBlockState correctFacing(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing enumfacing = null;

        for (EnumFacing horizontal : EnumFacing.Plane.HORIZONTAL) {
            IBlockState iblockstate = worldIn.getBlockState(pos.offset(horizontal));

            if (isType(type, worldIn, pos.offset(horizontal))) {
                return state;
            }

            if (iblockstate.isFullBlock()) {
                if (enumfacing != null) {
                    enumfacing = null;
                    break;
                }

                enumfacing = horizontal;
            }
        }

        if (enumfacing != null) {
            return state.withProperty(FACING, enumfacing.getOpposite());
        } else {
            EnumFacing current = state.getValue(FACING);

            if (worldIn.getBlockState(pos.offset(current)).isFullBlock()) {
                current = current.getOpposite();
            }

            if (worldIn.getBlockState(pos.offset(current)).isFullBlock()) {
                current = current.rotateY();
            }

            if (worldIn.getBlockState(pos.offset(current)).isFullBlock()) {
                current = current.getOpposite();
            }

            return state.withProperty(FACING, current);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        int i = 0;
        BlockPos westBP = pos.west();
        BlockPos eastBP = pos.east();
        BlockPos northBP = pos.north();
        BlockPos southBP = pos.south();

        if (isType(type, worldIn, westBP)) {
            if (this.isDoubleChest(worldIn, westBP)) {
                return false;
            }
            ++i;
        }

        if (isType(type, worldIn, eastBP)) {
            if (this.isDoubleChest(worldIn, eastBP)) {
                return false;
            }
            ++i;
        }

        if (isType(type, worldIn, northBP)) {
            if (this.isDoubleChest(worldIn, northBP)) {
                return false;
            }
            ++i;
        }

        if (isType(type, worldIn, southBP)) {
            if (this.isDoubleChest(worldIn, southBP)) {
                return false;
            }
            ++i;
        }

        return i <= 1;
    }

    public boolean isDoubleChest(World worldIn, BlockPos pos) {
        if (!isType(type, worldIn, pos)) {
            return false;
        } else {
            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                if (isType(type, worldIn, pos.offset(enumfacing))) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityStoneChest();
    }

    public boolean isType(EnumStoneChest type, IBlockAccess source, BlockPos pos) {
        EnumStoneChest other = getType(source, pos);
        return other != null && other == type;
    }

    public EnumStoneChest getType(IBlockAccess source, BlockPos pos) {
        if (source.getBlockState(pos).getBlock() == this)
            return ((BlockStoneChest) source.getBlockState(pos).getBlock()).type;
        return null;
    }

    @Override
    public boolean isBurning(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Nullable
    @Override
    public ILockableContainer getContainer(World worldIn, BlockPos pos, boolean allowBlocking) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (!(tileentity instanceof TileEntityStoneChest)) {
            return null;
        } else {
            ILockableContainer ilockablecontainer = (TileEntityStoneChest) tileentity;

            if (!allowBlocking && this.isBlocked(worldIn, pos)) {
                return null;
            } else {
                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                    BlockPos blockpos = pos.offset(enumfacing);

                    if (isType(type, worldIn, blockpos)) {
                        if (!allowBlocking && this.isBlocked(worldIn, blockpos)) // Forge: fix MC-99321
                        {
                            return null;
                        }

                        TileEntity tileentity1 = worldIn.getTileEntity(blockpos);

                        if (tileentity1 instanceof TileEntityStoneChest) {
                            if (enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH) {
                                ilockablecontainer = new InventoryLargeChest("container.chestDouble", ilockablecontainer, (TileEntityStoneChest) tileentity1);
                            } else {
                                ilockablecontainer = new InventoryLargeChest("container.chestDouble", (TileEntityStoneChest) tileentity1, ilockablecontainer);
                            }
                        }
                    }
                }

                return ilockablecontainer;
            }
        }
    }

    private boolean isBlocked(World worldIn, BlockPos pos) {
        return this.isBelowSolidBlock(worldIn, pos) || this.isOcelotSittingOnChest(worldIn, pos);
    }

    private boolean isBelowSolidBlock(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.up()).doesSideBlockChestOpening(worldIn, pos.up(), EnumFacing.DOWN);
    }

    private boolean isOcelotSittingOnChest(World worldIn, BlockPos pos) {
        for (Entity entity : worldIn.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB((double) pos.getX(), (double) (pos.getY() + 1), (double) pos.getZ(), (double) (pos.getX() + 1), (double) (pos.getY() + 2), (double) (pos.getZ() + 1)))) {
            EntityOcelot entityocelot = (EntityOcelot) entity;

            if (entityocelot.isSitting()) {
                return true;
            }
        }
        return false;
    }
}
