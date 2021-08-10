package ftblag.stonechest.blocks;

import ftblag.stonechest.StoneChest;
import ftblag.stonechest.tileentities.TileEntityStoneChest;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolType;

public class BlockStoneChest extends ChestBlock {
    private final EnumStoneChest chestType;

    public BlockStoneChest(EnumStoneChest chestType) {
        super(Properties.of(Material.STONE).strength(2.5F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE), () -> StoneChest.chestTileType);
        this.chestType = chestType;
        this.setRegistryName(StoneChest.MODID, "chest_" + chestType.name().toLowerCase());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityStoneChest(this.chestType, pos, state);
    }

    public EnumStoneChest getChestType() {
        return this.chestType;
    }
}
