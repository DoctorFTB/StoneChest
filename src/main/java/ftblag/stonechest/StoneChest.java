package ftblag.stonechest;

import ftblag.stonechest.blocks.BlockStoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import ftblag.stonechest.tileentities.TileEntityStoneChest;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;

@Mod(StoneChest.MODID)
public class StoneChest {
    public static final String MODID = "stonechest";
    public static BlockStoneChest[] chests = new BlockStoneChest[EnumStoneChest.values().length];
    public static Item[] parts = new Item[EnumStoneChest.values().length];
    public static BlockEntityType<TileEntityStoneChest> chestTileType;

    public StoneChest() {
        SCEventHandler.init();

        for (EnumStoneChest type : EnumStoneChest.values()) {
            parts[type.ordinal()] = new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)).setRegistryName(MODID, "part_" + type.name().toLowerCase());
            chests[type.ordinal()] = new BlockStoneChest(type);
        }

        chestTileType = BlockEntityType.Builder.of(TileEntityStoneChest::new, chests).build(null);
        chestTileType.setRegistryName(MODID, "chest_tile");
    }
}
