package ftblag.stonechest;

import ftblag.stonechest.blocks.BlockStoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import ftblag.stonechest.client.TEISRStoneChest;
import ftblag.stonechest.tileentities.TileEntityStoneChest;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;

public class SCRegistry {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, StoneChest.MODID);
    private static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, StoneChest.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, StoneChest.MODID);

    public static RegistryObject<BlockStoneChest>[] chests = new RegistryObject[EnumStoneChest.VALUES.length];
    public static RegistryObject<BlockEntityType<TileEntityStoneChest>> CHEST_TILE_TYPE;

    public static void register(IEventBus modEventBus) {
        for (EnumStoneChest type : EnumStoneChest.VALUES) {
            ITEMS.register("part_" + type.name().toLowerCase(Locale.ENGLISH), () -> new Item(new Item.Properties().setId(getKeyForItem("part_" + type.name().toLowerCase(Locale.ENGLISH)))));

            String name = "chest_" + type.name().toLowerCase(Locale.ENGLISH);

            RegistryObject<BlockStoneChest> chestObject = BLOCKS.register(name, () -> new BlockStoneChest(type, getKeyForBlock(name)));
            chests[type.ordinal()] = chestObject;

            ITEMS.register(name, () -> new BlockItem(chestObject.get(), new Item.Properties().setId(getKeyForItem(name))){
                @Override
                public void initializeClient(Consumer<IClientItemExtensions> consumer) {
                    super.initializeClient(consumer);

                    consumer.accept(new IClientItemExtensions() {
                        @Override
                        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                            return TEISRStoneChest.INSTANCE;
                        }
                    });
                }
            });
        }

        CHEST_TILE_TYPE = TILE_ENTITIES.register("chest_tile", () -> new BlockEntityType<>(TileEntityStoneChest::new, Set.of((Arrays.stream(chests).map(RegistryObject::get).toArray(Block[]::new)))));

        BLOCKS.register(modEventBus);
        TILE_ENTITIES.register(modEventBus);
        ITEMS.register(modEventBus);
    }

    public static ResourceKey<Item> getKeyForItem(String path) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(StoneChest.MODID, path));
    }

    public static ResourceKey<Block> getKeyForBlock(String path) {
        return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(StoneChest.MODID, path));
    }
}
