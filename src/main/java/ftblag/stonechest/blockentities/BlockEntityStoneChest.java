package ftblag.stonechest.blockentities;

import ftblag.stonechest.SCRegistry;
import ftblag.stonechest.blocks.EnumStoneChest;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityStoneChest extends ChestBlockEntity {

    private EnumStoneChest chestType;

    public BlockEntityStoneChest(BlockPos pos, BlockState state) {
        this(EnumStoneChest.COBBLESTONE, pos, state);
    }

    public BlockEntityStoneChest(EnumStoneChest chestType, BlockPos pos, BlockState state) {
        super(SCRegistry.CHEST_BLOCK_ENTITY_TYPE, pos, state);
        this.chestType = chestType;
    }

    public EnumStoneChest getChestType() {
        return chestType;
    }
}
