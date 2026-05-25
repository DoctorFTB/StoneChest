package ftblag.stonechest.client;

import ftblag.stonechest.StoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import ftblag.stonechest.blockentities.BlockEntityStoneChest;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.properties.ChestType;

import java.util.Locale;

public class BlockEntityStoneChestRenderer extends ChestRenderer<BlockEntityStoneChest> {
    public static SpriteId[] single = new SpriteId[EnumStoneChest.VALUES.length];
    public static SpriteId[] left = new SpriteId[EnumStoneChest.VALUES.length];
    public static SpriteId[] right = new SpriteId[EnumStoneChest.VALUES.length];

    static {
        for (EnumStoneChest type : EnumStoneChest.VALUES) {
            single[type.ordinal()] = getChestSpriteId(type.name().toLowerCase(Locale.ENGLISH));
            left[type.ordinal()] = getChestSpriteId(type.name().toLowerCase(Locale.ENGLISH) + "_left");
            right[type.ordinal()] = getChestSpriteId(type.name().toLowerCase(Locale.ENGLISH) + "_right");
        }
    }

    public BlockEntityStoneChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SpriteId getCustomSprite(BlockEntityStoneChest blockEntity, ChestRenderState renderState) {
        return getChestSpriteId(blockEntity, renderState.type);
    }

    private static SpriteId getChestSpriteId(String path) {
        return new SpriteId(Sheets.CHEST_SHEET, Identifier.fromNamespaceAndPath(StoneChest.MODID, "entity/chest/" + path));
    }

    private static SpriteId getChestSpriteId(BlockEntityStoneChest entity, ChestType type) {
        switch(type) {
            case LEFT:
                return left[entity.getChestType().ordinal()];
            case RIGHT:
                return right[entity.getChestType().ordinal()];
            case SINGLE:
            default:
                return single[entity.getChestType().ordinal()];
        }
    }
}
