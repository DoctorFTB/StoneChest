package ftblag.stonechest;

import ftblag.stonechest.blocks.BlockStoneChest;
import ftblag.stonechest.client.TEISRStoneChest;
import ftblag.stonechest.client.TileEntityStoneChestRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class SCEventHandler {
    public static void init() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addGenericListener(Block.class, SCEventHandler::onBlockRegistry);
        modBus.addGenericListener(Item.class, SCEventHandler::onItemRegistry);
        modBus.addGenericListener(TileEntityType.class, SCEventHandler::onTileRegistry);
        if (FMLEnvironment.dist.isClient()) {
            modBus.addListener(SCEventHandler::onTextureStitch);
            modBus.addListener(SCEventHandler::doClientStuff);
        }
    }

    public static void onBlockRegistry(Register<Block> event) {
        for (BlockStoneChest chest : StoneChest.chests) {
            event.getRegistry().register(chest);
        }
    }

    public static void onItemRegistry(Register<Item> event) {
        for (BlockStoneChest chest : StoneChest.chests) {
            event.getRegistry().register(new BlockItem(chest, new Item.Properties().tab(ItemGroup.TAB_DECORATIONS).setISTER(() -> TEISRStoneChest::new)).setRegistryName(chest.getRegistryName()));
        }
        for (Item part : StoneChest.parts) {
            event.getRegistry().register(part);
        }
    }

    public static void onTileRegistry(Register<TileEntityType<?>> event) {
        event.getRegistry().register(StoneChest.chestTileType);
    }

    @OnlyIn(Dist.CLIENT)
    public static void doClientStuff(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(StoneChest.chestTileType, TileEntityStoneChestRenderer::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().location().equals(Atlases.CHEST_SHEET)) {
            for (RenderMaterial material : TileEntityStoneChestRenderer.single) {
                event.addSprite(material.texture());
            }
            for (RenderMaterial material : TileEntityStoneChestRenderer.left) {
                event.addSprite(material.texture());
            }
            for (RenderMaterial material : TileEntityStoneChestRenderer.right) {
                event.addSprite(material.texture());
            }
        }
    }
}
