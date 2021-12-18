package ftblag.stonechest;

import ftblag.stonechest.blocks.BlockStoneChest;
import ftblag.stonechest.client.TEISRStoneChest;
import ftblag.stonechest.client.TileEntityStoneChestRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.function.Consumer;

public class SCEventHandler {
    public static void init() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addGenericListener(Block.class, SCEventHandler::onBlockRegistry);
        modBus.addGenericListener(Item.class, SCEventHandler::onItemRegistry);
        modBus.addGenericListener(BlockEntityType.class, SCEventHandler::onTileRegistry);
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
            event.getRegistry().register(new BlockItem(chest, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)){
                @Override
                public void initializeClient(Consumer<IItemRenderProperties> consumer) {
                    super.initializeClient(consumer);

                    consumer.accept(new IItemRenderProperties() {
                        @Override
                        public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                            return TEISRStoneChest.INSTANCE;
                        }
                    });
                }
            }.setRegistryName(chest.getRegistryName()));
        }
        for (Item part : StoneChest.parts) {
            event.getRegistry().register(part);
        }
    }

    public static void onTileRegistry(Register<BlockEntityType<?>> event) {
        event.getRegistry().register(StoneChest.chestTileType);
    }

    @OnlyIn(Dist.CLIENT)
    public static void doClientStuff(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(StoneChest.chestTileType, TileEntityStoneChestRenderer::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(Sheets.CHEST_SHEET)) {
            for (Material material : TileEntityStoneChestRenderer.single) {
                event.addSprite(material.texture());
            }
            for (Material material : TileEntityStoneChestRenderer.left) {
                event.addSprite(material.texture());
            }
            for (Material material : TileEntityStoneChestRenderer.right) {
                event.addSprite(material.texture());
            }
        }
    }
}
