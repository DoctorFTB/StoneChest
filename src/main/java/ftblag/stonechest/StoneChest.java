package ftblag.stonechest;

import ftblag.stonechest.blocks.BlockStoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import ftblag.stonechest.client.TEISRChest;
import ftblag.stonechest.client.TileEntityStoneChestRenderer;
import ftblag.stonechest.items.ItemChestPart;
import ftblag.stonechest.tileentities.TileEntityStoneChest;
import net.minecraft.block.BlockStone;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = StoneChest.MODID, name = StoneChest.NAME, version = "@VERSION@")
public class StoneChest {

    public static final String MODID = "stonechest", NAME = "Stone Chest";
    public static ItemChestPart[] parts = new ItemChestPart[EnumStoneChest.values().length];
    public static BlockStoneChest[] chests = new BlockStoneChest[EnumStoneChest.values().length];

    private static ItemStack getBlock(EnumStoneChest type) {
        switch (type) {
            case STONE:
                return new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.STONE.getMetadata());
            case GRANITE:
                return new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.GRANITE.getMetadata());
            case DIORITE:
                return new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.DIORITE.getMetadata());
            case ANDESITE:
                return new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.ANDESITE.getMetadata());
            case COBBLESTONE:
            default:
                return new ItemStack(Blocks.COBBLESTONE);
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        GameRegistry.registerTileEntity(TileEntityStoneChest.class, MODID + ":chest");
        for (EnumStoneChest type : EnumStoneChest.values()) {
            String partName = "part_" + type.name().toLowerCase();
            ItemChestPart part = new ItemChestPart(partName);
            GameRegistry.addShapedRecipe(new ResourceLocation(MODID + ":" + partName), null, new ItemStack(part), "# ", " #", '#', getBlock(type));
            parts[type.ordinal()] = part;

            String chestName = "chest_" + type.name().toLowerCase();
            BlockStoneChest chest = new BlockStoneChest(type, chestName);
            ForgeRegistries.BLOCKS.register(chest);
            ForgeRegistries.ITEMS.registerAll(part, new ItemBlock(chest) {
                @Override
                public int getItemBurnTime(ItemStack itemStack) {
                    return 0;
                }
            }.setRegistryName(chest.getRegistryName()));
            OreDictionary.registerOre("chest", chest);
            GameRegistry.addShapedRecipe(new ResourceLocation(MODID + ":" + chestName), null, new ItemStack(chest), "##", "##", '#', part);
            chests[type.ordinal()] = chest;
        }
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            preInit();
    }

    @SideOnly(Side.CLIENT)
    public void preInit() {
        TEISRChest TEISR = new TEISRChest();
        for (BlockStoneChest chest : chests) {
            Item item = Item.getItemFromBlock(chest);
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(chest.getRegistryName(), "inventory"));
            item.setTileEntityItemStackRenderer(TEISR);
        }
        for (ItemChestPart part : parts) {
            ModelLoader.setCustomModelResourceLocation(part, 0, new ModelResourceLocation(part.getRegistryName(), "inventory"));
        }
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStoneChest.class, new TileEntityStoneChestRenderer());
    }
}
