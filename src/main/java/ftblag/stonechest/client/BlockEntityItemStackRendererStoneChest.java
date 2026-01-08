package ftblag.stonechest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import ftblag.stonechest.SCRegistry;
import ftblag.stonechest.blocks.BlockStoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import ftblag.stonechest.blockentities.BlockEntityStoneChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class BlockEntityItemStackRendererStoneChest extends BlockEntityWithoutLevelRenderer {
    public static final BlockEntityItemStackRendererStoneChest INSTANCE = new BlockEntityItemStackRendererStoneChest();

    private BlockEntityStoneChest[] entities = new BlockEntityStoneChest[EnumStoneChest.VALUES.length];

    {
        for (EnumStoneChest type : EnumStoneChest.VALUES)
            entities[type.ordinal()] = new BlockEntityStoneChest(type, BlockPos.ZERO, SCRegistry.chests[type.ordinal()].get().defaultBlockState());
    }

    public BlockEntityItemStackRendererStoneChest() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Block block = Block.byItem(stack.getItem());
        if (block instanceof BlockStoneChest) {
            Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(this.entities[((BlockStoneChest)block).getChestType().ordinal()], poseStack, buffer, packedLight, packedOverlay);
        } else {
            super.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
        }
    }
}
