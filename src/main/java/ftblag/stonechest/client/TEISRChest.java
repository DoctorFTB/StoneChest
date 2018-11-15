package ftblag.stonechest.client;

import ftblag.stonechest.blocks.BlockStoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import ftblag.stonechest.tileentities.TileEntityStoneChest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class TEISRChest extends TileEntityItemStackRenderer {

    private TileEntityStoneChest[] tiles = new TileEntityStoneChest[EnumStoneChest.values().length];

    {
        for (EnumStoneChest type : EnumStoneChest.values())
            tiles[type.ordinal()] = new TileEntityStoneChest(type);
    }

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        renderByItem(itemStackIn, 1F);
    }

    @Override
    public void renderByItem(ItemStack itemStackIn, float partialTicks) {
        Block block = Block.getBlockFromItem(itemStackIn.getItem());
        if (block instanceof BlockStoneChest)
            TileEntityRendererDispatcher.instance.render(tiles[((BlockStoneChest) block).type.ordinal()], 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
        else
            net.minecraftforge.client.ForgeHooksClient.renderTileItem(itemStackIn.getItem(), itemStackIn.getMetadata());
    }
}
