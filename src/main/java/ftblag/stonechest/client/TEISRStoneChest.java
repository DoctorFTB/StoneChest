package ftblag.stonechest.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import ftblag.stonechest.blocks.BlockStoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import ftblag.stonechest.tileentities.TileEntityStoneChest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class TEISRStoneChest extends ItemStackTileEntityRenderer {
    private TileEntityStoneChest[] tiles = new TileEntityStoneChest[EnumStoneChest.values().length];

    {
        for (EnumStoneChest type : EnumStoneChest.values())
            tiles[type.ordinal()] = new TileEntityStoneChest(type);
    }

    @Override
    public void renderByItem(ItemStack itemStackIn, TransformType transformTypeIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Block block = Block.byItem(itemStackIn.getItem());
        if (block instanceof BlockStoneChest) {
            TileEntityRendererDispatcher.instance.renderItem(this.tiles[((BlockStoneChest)block).getChestType().ordinal()], matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        } else {
            super.renderByItem(itemStackIn, transformTypeIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
    }
}
