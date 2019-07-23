package ftblag.stonechest.client;

import com.mojang.blaze3d.platform.GlStateManager;
import ftblag.stonechest.StoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import ftblag.stonechest.tileentities.TileEntityStoneChest;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.model.ChestModel;
import net.minecraft.client.renderer.tileentity.model.LargeChestModel;
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public class TileEntityStoneChestRenderer extends TileEntityRenderer<TileEntityStoneChest> {

    private static ResourceLocation[] normals = new ResourceLocation[EnumStoneChest.values().length];
    private static ResourceLocation[] doubles = new ResourceLocation[EnumStoneChest.values().length];
    private final ChestModel simpleChest = new ChestModel();
    private final ChestModel largeChest = new LargeChestModel();

    static {
        for (EnumStoneChest type : EnumStoneChest.values()) {
            normals[type.ordinal()] = new ResourceLocation(StoneChest.MODID, "textures/entity/" + type.name().toLowerCase() + ".png");
            doubles[type.ordinal()] = new ResourceLocation(StoneChest.MODID, "textures/entity/" + type.name().toLowerCase() + "_double.png");
        }
    }

    public void render(TileEntityStoneChest tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.enableDepthTest();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        BlockState blockstate = tileEntityIn.hasWorld() ? tileEntityIn.getBlockState() : Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
        ChestType chesttype = blockstate.has(ChestBlock.TYPE) ? blockstate.get(ChestBlock.TYPE) : ChestType.SINGLE;
        if (chesttype != ChestType.LEFT) {
            boolean flag = chesttype != ChestType.SINGLE;
            ChestModel chestmodel = this.getChestModel(tileEntityIn, destroyStage, flag);
            if (destroyStage >= 0) {
                GlStateManager.matrixMode(5890);
                GlStateManager.pushMatrix();
                GlStateManager.scalef(flag ? 8.0F : 4.0F, 4.0F, 1.0F);
                GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
                GlStateManager.matrixMode(5888);
            } else {
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            }

            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            GlStateManager.translatef((float)x, (float)y + 1.0F, (float)z + 1.0F);
            GlStateManager.scalef(1.0F, -1.0F, -1.0F);
            float f = blockstate.get(ChestBlock.FACING).getHorizontalAngle();
            if ((double)Math.abs(f) > 1.0E-5D) {
                GlStateManager.translatef(0.5F, 0.5F, 0.5F);
                GlStateManager.rotatef(f, 0.0F, 1.0F, 0.0F);
                GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
            }

            this.applyLidRotation(tileEntityIn, partialTicks, chestmodel);
            chestmodel.renderAll();
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (destroyStage >= 0) {
                GlStateManager.matrixMode(5890);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
            }

        }
    }

    private ChestModel getChestModel(TileEntityStoneChest tile, int destroyStage, boolean doubleChest) {
        ResourceLocation resourcelocation;
        if (destroyStage >= 0) {
            resourcelocation = DESTROY_STAGES[destroyStage];
        } else {
            resourcelocation = doubleChest ? doubles[tile.getChestType().ordinal()] : normals[tile.getChestType().ordinal()];
        }

        this.bindTexture(resourcelocation);
        return doubleChest ? this.largeChest : this.simpleChest;
    }

    private void applyLidRotation(TileEntityStoneChest tile, float p_199346_2_, ChestModel model) {
        float f = tile.getLidAngle(p_199346_2_);
        f = 1.0F - f;
        f = 1.0F - f * f * f;
        model.getLid().rotateAngleX = -(f * ((float)Math.PI / 2F));
    }
}
