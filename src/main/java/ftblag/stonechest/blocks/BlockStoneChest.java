package ftblag.stonechest.blocks;

import ftblag.stonechest.SCRegistry;
import ftblag.stonechest.blockentities.BlockEntityStoneChest;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStoneChest extends ChestBlock {
    private final EnumStoneChest chestType;

    public BlockStoneChest(EnumStoneChest chestType, ResourceKey<Block> key) {
        super(() -> SCRegistry.CHEST_BLOCK_ENTITY_TYPE.get(), Properties.of().strength(2.5F).sound(SoundType.STONE).setId(key));
        this.chestType = chestType;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityStoneChest(this.chestType, pos, state);
    }

    public EnumStoneChest getChestType() {
        return this.chestType;
    }
}
