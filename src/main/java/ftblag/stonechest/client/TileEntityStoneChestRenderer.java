package ftblag.stonechest.client;

import ftblag.stonechest.StoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import ftblag.stonechest.tileentities.TileEntityStoneChest;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileEntityStoneChestRenderer extends ChestTileEntityRenderer<TileEntityStoneChest> {
    public static RenderMaterial[] single = new RenderMaterial[EnumStoneChest.values().length];
    public static RenderMaterial[] left = new RenderMaterial[EnumStoneChest.values().length];
    public static RenderMaterial[] right = new RenderMaterial[EnumStoneChest.values().length];

    static {
        for (EnumStoneChest type : EnumStoneChest.values()) {
            single[type.ordinal()] = getChestMaterial(type.name().toLowerCase());
            left[type.ordinal()] = getChestMaterial(type.name().toLowerCase() + "_left");
            right[type.ordinal()] = getChestMaterial(type.name().toLowerCase() + "_right");
        }
    }

    public TileEntityStoneChestRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    protected RenderMaterial getMaterial(TileEntityStoneChest tileEntity, ChestType chestType) {
        return getChestMaterial(tileEntity, chestType);
    }

    private static RenderMaterial getChestMaterial(String path) {
        return new RenderMaterial(Atlases.CHEST_SHEET, new ResourceLocation(StoneChest.MODID, "entity/chest/" + path));
    }

    private static RenderMaterial getChestMaterial(TileEntityStoneChest tile, ChestType type) {
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
