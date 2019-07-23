package ftblag.stonechest.blocks;

import ftblag.stonechest.StoneChest;
import ftblag.stonechest.tileentities.TileEntityStoneChest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class BlockStoneChest extends ChestBlock {

    private final EnumStoneChest chestType;

    public BlockStoneChest(EnumStoneChest chestType) {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.5F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE));
        this.chestType = chestType;
        setRegistryName(StoneChest.MODID, "chest_" + chestType.name().toLowerCase());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityStoneChest(chestType);
    }

    public EnumStoneChest getChestType() {
        return chestType;
    }
}
