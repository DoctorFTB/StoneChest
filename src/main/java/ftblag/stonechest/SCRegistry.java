package ftblag.stonechest;

import ftblag.stonechest.blocks.BlockStoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import ftblag.stonechest.blockentities.BlockEntityStoneChest;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.transfer.CombinedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;

import java.util.Arrays;
import java.util.Locale;

public class SCRegistry {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, StoneChest.MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, StoneChest.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, StoneChest.MODID);

    public static DeferredHolder<Block, BlockStoneChest>[] chests = new DeferredHolder[EnumStoneChest.VALUES.length];
    public static DeferredHolder<Item, BlockItem>[] chestItems = new DeferredHolder[EnumStoneChest.VALUES.length];
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityStoneChest>> CHEST_BLOCK_ENTITY_TYPE;

    private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, ResourceHandler<ItemResource>> CHEST_COMBINER_HANDLER = new DoubleBlockCombiner.Combiner<>() {
        @Override
        public ResourceHandler<ItemResource> acceptDouble(ChestBlockEntity chest1, ChestBlockEntity chest2) {
            return new CombinedResourceHandler<>(VanillaContainerWrapper.of(chest1), VanillaContainerWrapper.of(chest2));
        }

        @Override
        public ResourceHandler<ItemResource> acceptSingle(ChestBlockEntity chest) {
            return VanillaContainerWrapper.of(chest);
        }

        @Override
        public ResourceHandler<ItemResource> acceptNone() {
            return null;
        }
    };

    public static void register(IEventBus modEventBus) {
        for (EnumStoneChest type : EnumStoneChest.VALUES) {
            ITEMS.register("part_" + type.name().toLowerCase(Locale.ENGLISH), () -> new Item(new Item.Properties().setId(getKeyForItem("part_" + type.name().toLowerCase(Locale.ENGLISH)))));

            String name = "chest_" + type.name().toLowerCase(Locale.ENGLISH);

            DeferredHolder<Block, BlockStoneChest> chestObject = BLOCKS.register(name, () -> new BlockStoneChest(type, getKeyForBlock(name)));
            chests[type.ordinal()] = chestObject;

            DeferredHolder<Item, BlockItem> chestItemObject = ITEMS.register(name, () -> new BlockItem(chestObject.get(), new Item.Properties().setId(getKeyForItem(name))));
            chestItems[type.ordinal()] = chestItemObject;
        }

        CHEST_BLOCK_ENTITY_TYPE = BLOCK_ENTITIES.register("chest_tile", () -> new BlockEntityType<>(BlockEntityStoneChest::new, Arrays.stream(chests).map(DeferredHolder::get).toArray(Block[]::new)));

        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        ITEMS.register(modEventBus);

        modEventBus.addListener((RegisterCapabilitiesEvent event) -> {
            event.registerBlock(
                    Capabilities.Item.BLOCK,
                    (level, pos, state, be, side) -> ((ChestBlock) state.getBlock()).combine(state, level, pos, true).apply(CHEST_COMBINER_HANDLER),
                    Arrays.stream(chests).map(DeferredHolder::get).toArray(Block[]::new)
            );
        });
    }

    public static ResourceKey<Item> getKeyForItem(String path) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(StoneChest.MODID, path));
    }

    public static ResourceKey<Block> getKeyForBlock(String path) {
        return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(StoneChest.MODID, path));
    }
}
