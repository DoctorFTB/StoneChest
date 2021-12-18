package ftblag.stonechest.tileentities;

import ftblag.stonechest.StoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityStoneChest extends ChestBlockEntity {

    private EnumStoneChest chestType;

    public TileEntityStoneChest(BlockPos pos, BlockState state) {
        this(EnumStoneChest.COBBLESTONE, pos, state);
    }

    public TileEntityStoneChest(EnumStoneChest chestType, BlockPos pos, BlockState state) {
        super(StoneChest.chestTileType, pos, state);
        this.chestType = chestType;
    }

    public EnumStoneChest getChestType() {
        return chestType;
    }
}
