package ftblag.stonechest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import ftblag.stonechest.StoneChest;
import ftblag.stonechest.blocks.BlockStoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import ftblag.stonechest.tileentities.TileEntityStoneChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class TEISRStoneChest extends BlockEntityWithoutLevelRenderer {
    public static final TEISRStoneChest INSTANCE = new TEISRStoneChest();

    private TileEntityStoneChest[] tiles = new TileEntityStoneChest[EnumStoneChest.values().length];

    {
        for (EnumStoneChest type : EnumStoneChest.values())
            tiles[type.ordinal()] = new TileEntityStoneChest(type, BlockPos.ZERO, StoneChest.chests[type.ordinal()].defaultBlockState());
    }

    public TEISRStoneChest() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
    }

    @Override
    public void renderByItem(ItemStack itemStackIn, ItemTransforms.TransformType transformTypeIn, PoseStack stack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Block block = Block.byItem(itemStackIn.getItem());
        if (block instanceof BlockStoneChest) {
            Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(this.tiles[((BlockStoneChest)block).getChestType().ordinal()], stack, bufferIn, combinedLightIn, combinedOverlayIn);
        } else {
            super.renderByItem(itemStackIn, transformTypeIn, stack, bufferIn, combinedLightIn, combinedOverlayIn);
        }
    }
}