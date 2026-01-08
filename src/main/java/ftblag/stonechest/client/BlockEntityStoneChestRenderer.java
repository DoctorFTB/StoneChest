package ftblag.stonechest.client;

import ftblag.stonechest.StoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import ftblag.stonechest.blockentities.BlockEntityStoneChest;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.ChestType;

import java.util.Locale;

public class BlockEntityStoneChestRenderer extends ChestRenderer<BlockEntityStoneChest> {
    public static Material[] single = new Material[EnumStoneChest.VALUES.length];
    public static Material[] left = new Material[EnumStoneChest.VALUES.length];
    public static Material[] right = new Material[EnumStoneChest.VALUES.length];

    static {
        for (EnumStoneChest type : EnumStoneChest.VALUES) {
            single[type.ordinal()] = getChestMaterial(type.name().toLowerCase(Locale.ENGLISH));
            left[type.ordinal()] = getChestMaterial(type.name().toLowerCase(Locale.ENGLISH) + "_left");
            right[type.ordinal()] = getChestMaterial(type.name().toLowerCase(Locale.ENGLISH) + "_right");
        }
    }

    public BlockEntityStoneChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Material getCustomMaterial(BlockEntityStoneChest blockEntity, ChestRenderState renderState) {
        return getChestMaterial(blockEntity, renderState.type);
    }

    private static Material getChestMaterial(String path) {
        return new Material(Sheets.CHEST_SHEET, ResourceLocation.fromNamespaceAndPath(StoneChest.MODID, "entity/chest/" + path));
    }

    private static Material getChestMaterial(BlockEntityStoneChest entity, ChestType type) {
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
