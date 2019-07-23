package ftblag.stonechest;

import ftblag.stonechest.blocks.BlockStoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import ftblag.stonechest.tileentities.TileEntityStoneChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.common.Mod;

@Mod(StoneChest.MODID)
public class StoneChest {

    public static final String MODID = "stonechest";
    public static BlockStoneChest[] chests = new BlockStoneChest[EnumStoneChest.values().length];
    public static Item[] parts = new Item[EnumStoneChest.values().length];
    public static TileEntityType<TileEntityStoneChest> chestTileType;

    public StoneChest() {
        for (EnumStoneChest type : EnumStoneChest.values()) {
            parts[type.ordinal()] = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(MODID, "part_" + type.name().toLowerCase());
            chests[type.ordinal()] = new BlockStoneChest(type);
        }
        chestTileType = TileEntityType.Builder.create(TileEntityStoneChest::new, chests).build(null);
        chestTileType.setRegistryName(MODID, "chest_tile");
    }
}
