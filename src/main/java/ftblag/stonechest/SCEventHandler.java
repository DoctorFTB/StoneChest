package ftblag.stonechest;

import ftblag.stonechest.blocks.BlockStoneChest;
import ftblag.stonechest.client.TEISRStoneChest;
import ftblag.stonechest.client.TileEntityStoneChestRenderer;
import ftblag.stonechest.tileentities.TileEntityStoneChest;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = StoneChest.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SCEventHandler {

    @SubscribeEvent
    public static void onBlockRegistry(RegistryEvent.Register<Block> event) {
        for (BlockStoneChest chest : StoneChest.chests) {
            event.getRegistry().register(chest);
        }
    }

    @SubscribeEvent
    public static void onItemRegistry(RegistryEvent.Register<Item> event) {
        for (BlockStoneChest chest : StoneChest.chests) {
            event.getRegistry().register(new BlockItem(chest, new Item.Properties().group(ItemGroup.DECORATIONS).setTEISR(() -> TEISRStoneChest::new)).setRegistryName(chest.getRegistryName()));
        }
        for (Item part : StoneChest.parts) {
            event.getRegistry().register(part);
        }
    }

    @SubscribeEvent
    public static void onTileRegistry(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(StoneChest.chestTileType);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void doClientStuff(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStoneChest.class, new TileEntityStoneChestRenderer());
    }
}
