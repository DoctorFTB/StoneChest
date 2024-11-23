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
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Consumer;

public class SCRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, StoneChest.MODID);
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, StoneChest.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, StoneChest.MODID);

    public static DeferredHolder<Block, BlockStoneChest>[] chests = new DeferredHolder[EnumStoneChest.VALUES.length];
    public static DeferredHolder<Item, BlockItem>[] chestItems = new DeferredHolder[EnumStoneChest.VALUES.length];
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityStoneChest>> CHEST_TILE_TYPE;

    public static void register(IEventBus modEventBus) {
        for (EnumStoneChest type : EnumStoneChest.VALUES) {
            ITEMS.register("part_" + type.name().toLowerCase(Locale.ENGLISH), () -> new Item(new Item.Properties().setId(getKeyForItem("part_" + type.name().toLowerCase(Locale.ENGLISH)))));

            String name = "chest_" + type.name().toLowerCase(Locale.ENGLISH);

            DeferredHolder<Block, BlockStoneChest> chestObject = BLOCKS.register(name, () -> new BlockStoneChest(type, getKeyForBlock(name)));
            chests[type.ordinal()] = chestObject;

            DeferredHolder<Item, BlockItem> checkItem = ITEMS.register(name, () -> new BlockItem(chestObject.get(), new Item.Properties().setId(getKeyForItem(name))));
            chestItems[type.ordinal()] = checkItem;
        }

        CHEST_TILE_TYPE = TILE_ENTITIES.register("chest_tile", () -> new BlockEntityType<>(TileEntityStoneChest::new, Arrays.stream(chests).map(DeferredHolder::get).toArray(Block[]::new)));

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
