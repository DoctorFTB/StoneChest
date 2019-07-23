package ftblag.stonechest.client;

import ftblag.stonechest.blocks.BlockStoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import ftblag.stonechest.tileentities.TileEntityStoneChest;
import net.minecraft.block.Block;
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
    public void renderByItem(ItemStack stack) {
        Block block = Block.getBlockFromItem(stack.getItem());
        if (block instanceof BlockStoneChest) {
            TileEntityRendererDispatcher.instance.renderAsItem(tiles[((BlockStoneChest) block).getChestType().ordinal()]);
        } else {
            super.renderByItem(stack);
        }
    }
}
