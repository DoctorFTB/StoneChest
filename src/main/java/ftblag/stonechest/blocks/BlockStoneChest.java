package ftblag.stonechest.blocks;

import ftblag.stonechest.SCRegistry;
import ftblag.stonechest.tileentities.TileEntityStoneChest;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStoneChest extends ChestBlock {
    private final EnumStoneChest chestType;

    public BlockStoneChest(EnumStoneChest chestType) {
        super(Properties.of().strength(2.5F).sound(SoundType.STONE), () -> SCRegistry.CHEST_TILE_TYPE.get());
        this.chestType = chestType;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityStoneChest(this.chestType, pos, state);
    }

    public EnumStoneChest getChestType() {
        return this.chestType;
    }
}
