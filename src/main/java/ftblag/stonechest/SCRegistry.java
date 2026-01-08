package ftblag.stonechest;

import ftblag.stonechest.blocks.BlockStoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import ftblag.stonechest.client.BlockEntityItemStackRendererStoneChest;
import ftblag.stonechest.blockentities.BlockEntityStoneChest;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Consumer;

public class SCRegistry {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, StoneChest.MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, StoneChest.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, StoneChest.MODID);

    public static RegistryObject<BlockStoneChest>[] chests = new RegistryObject[EnumStoneChest.VALUES.length];
    public static RegistryObject<BlockEntityType<BlockEntityStoneChest>> CHEST_BLOCK_ENTITY_TYPE;

    public static void register(IEventBus modEventBus) {
        for (EnumStoneChest type : EnumStoneChest.VALUES) {
            ITEMS.register("part_" + type.name().toLowerCase(Locale.ENGLISH), () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

            String name = "chest_" + type.name().toLowerCase(Locale.ENGLISH);

            RegistryObject<BlockStoneChest> chestObject = BLOCKS.register(name, () -> new BlockStoneChest(type));
            chests[type.ordinal()] = chestObject;

            ITEMS.register(name, () -> new BlockItem(chestObject.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)){
                @Override
                public void initializeClient(Consumer<IClientItemExtensions> consumer) {
                    super.initializeClient(consumer);

                    consumer.accept(new IClientItemExtensions() {
                        @Override
                        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                            return BlockEntityItemStackRendererStoneChest.INSTANCE;
                        }
                    });
                }
            });
        }

        CHEST_BLOCK_ENTITY_TYPE = BLOCK_ENTITIES.register("chest_tile", () -> BlockEntityType.Builder.of(BlockEntityStoneChest::new, Arrays.stream(chests).map(RegistryObject::get).toArray(Block[]::new)).build(null));

        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        ITEMS.register(modEventBus);
    }
}
