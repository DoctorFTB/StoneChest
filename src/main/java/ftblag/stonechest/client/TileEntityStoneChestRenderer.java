package ftblag.stonechest.client;

import ftblag.stonechest.StoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import ftblag.stonechest.tileentities.TileEntityStoneChest;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileEntityStoneChestRenderer extends ChestRenderer<TileEntityStoneChest> {
    public static Material[] single = new Material[EnumStoneChest.values().length];
    public static Material[] left = new Material[EnumStoneChest.values().length];
    public static Material[] right = new Material[EnumStoneChest.values().length];

    static {
        for (EnumStoneChest type : EnumStoneChest.values()) {
            single[type.ordinal()] = getChestMaterial(type.name().toLowerCase());
            left[type.ordinal()] = getChestMaterial(type.name().toLowerCase() + "_left");
            right[type.ordinal()] = getChestMaterial(type.name().toLowerCase() + "_right");
        }
    }

    public TileEntityStoneChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Material getMaterial(TileEntityStoneChest blockEntity, ChestType chestType) {
        return getChestMaterial(blockEntity, chestType);
    }

    private static Material getChestMaterial(String path) {
        return new Material(Sheets.CHEST_SHEET, new ResourceLocation(StoneChest.MODID, "entity/chest/" + path));
    }

    private static Material getChestMaterial(TileEntityStoneChest tile, ChestType type) {
        switch(type) {
            case LEFT:
                return left[tile.getChestType().ordinal()];
            case RIGHT:
                return right[tile.getChestType().ordinal()];
            case SINGLE:
            default:
                return single[tile.getChestType().ordinal()];
        }
    }
}
