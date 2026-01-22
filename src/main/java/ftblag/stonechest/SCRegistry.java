package ftblag.stonechest;

import ftblag.stonechest.blocks.BlockStoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import ftblag.stonechest.blockentities.BlockEntityStoneChest;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Function;

public class SCRegistry {
    public static BlockStoneChest[] chests = new BlockStoneChest[EnumStoneChest.VALUES.length];
    public static Item[] parts = new Item[EnumStoneChest.VALUES.length];
    public static BlockEntityType<BlockEntityStoneChest> CHEST_BLOCK_ENTITY_TYPE;

    static {
        for (EnumStoneChest type : EnumStoneChest.VALUES) {
            parts[type.ordinal()] = registerItem("part_" + type.name().toLowerCase(Locale.ENGLISH), Item::new);

            chests[type.ordinal()] = registerBlock("chest_" + type.name().toLowerCase(Locale.ENGLISH), key -> new BlockStoneChest(type, key));
        }

        CHEST_BLOCK_ENTITY_TYPE = registerBlockEntity("chest_tile", BlockEntityStoneChest::new, chests);
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register((itemGroup) -> {
            Arrays.stream(parts).forEach(itemGroup::accept);
            Arrays.stream(chests).map(Block::asItem).forEach(itemGroup::accept);
        });
    }

    private static <T extends Item> T registerItem(String name, Function<Item.Properties, T> itemFactory) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(StoneChest.MODID, name));
        T item = itemFactory.apply(new Item.Properties().setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    private static <T extends Block> T registerBlock(String name, Function<ResourceKey<Block>, T> blockFactory) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(StoneChest.MODID, name));
        T block = blockFactory.apply(blockKey);

        registerItem(name, properties -> new BlockItem(block, properties));

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(
        String name,
        FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
        Block... blocks
    ) {
        Identifier id = Identifier.fromNamespaceAndPath(StoneChest.MODID, name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
}
