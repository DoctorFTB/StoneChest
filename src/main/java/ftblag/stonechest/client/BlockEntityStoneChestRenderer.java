package ftblag.stonechest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import ftblag.stonechest.StoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import ftblag.stonechest.blockentities.BlockEntityStoneChest;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public class BlockEntityStoneChestRenderer implements BlockEntityRenderer<BlockEntityStoneChest, LocalChestRenderState> {
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

    private final MaterialSet materials;
    private final ChestModel singleModel;
    private final ChestModel doubleLeftModel;
    private final ChestModel doubleRightModel;

    public BlockEntityStoneChestRenderer(BlockEntityRendererProvider.Context context) {
        this.materials = context.materials();
        this.singleModel = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
        this.doubleLeftModel = new ChestModel(context.bakeLayer(ModelLayers.DOUBLE_CHEST_LEFT));
        this.doubleRightModel = new ChestModel(context.bakeLayer(ModelLayers.DOUBLE_CHEST_RIGHT));
    }

    @Override
    public LocalChestRenderState createRenderState() {
        return new LocalChestRenderState();
    }

    @Override
    public void extractRenderState(BlockEntityStoneChest blockEntity, LocalChestRenderState renderState, float partialTick, Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        boolean flag = blockEntity.getLevel() != null;
        BlockState blockstate = flag ? blockEntity.getBlockState() : Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        renderState.type = blockstate.hasProperty(ChestBlock.TYPE) ? blockstate.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
        renderState.angle = blockstate.getValue(ChestBlock.FACING).toYRot();
        DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> neighborcombineresult;
        if (flag && blockstate.getBlock() instanceof ChestBlock chestblock) {
            neighborcombineresult = chestblock.combine(blockstate, blockEntity.getLevel(), blockEntity.getBlockPos(), true);
        } else {
            neighborcombineresult = DoubleBlockCombiner.Combiner::acceptNone;
        }

        renderState.open = neighborcombineresult.apply(ChestBlock.opennessCombiner(blockEntity)).get(partialTick);
        if (renderState.type != ChestType.SINGLE) {
            renderState.lightCoords = neighborcombineresult.apply(new BrightnessCombiner<>()).applyAsInt(renderState.lightCoords);
        }

        renderState.customMaterial = getChestMaterial(blockEntity, renderState);
    }

    @Override
    public void submit(LocalChestRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.angle));
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        float f = renderState.open;
        f = 1.0F - f;
        f = 1.0F - f * f * f;
        Material material = renderState.customMaterial;
        RenderType rendertype = material.renderType(RenderTypes::entityCutout);
        TextureAtlasSprite textureatlassprite = this.materials.get(material);
        if (renderState.type != ChestType.SINGLE) {
            if (renderState.type == ChestType.LEFT) {
                nodeCollector.submitModel(
                        this.doubleLeftModel,
                        f,
                        poseStack,
                        rendertype,
                        renderState.lightCoords,
                        OverlayTexture.NO_OVERLAY,
                        -1,
                        textureatlassprite,
                        0,
                        renderState.breakProgress
                );
            } else {
                nodeCollector.submitModel(
                        this.doubleRightModel,
                        f,
                        poseStack,
                        rendertype,
                        renderState.lightCoords,
                        OverlayTexture.NO_OVERLAY,
                        -1,
                        textureatlassprite,
                        0,
                        renderState.breakProgress
                );
            }
        } else {
            nodeCollector.submitModel(
                    this.singleModel,
                    f,
                    poseStack,
                    rendertype,
                    renderState.lightCoords,
                    OverlayTexture.NO_OVERLAY,
                    -1,
                    textureatlassprite,
                    0,
                    renderState.breakProgress
            );
        }

        poseStack.popPose();
    }

    private static Material getChestMaterial(String path) {
        return new Material(Sheets.CHEST_SHEET, Identifier.fromNamespaceAndPath(StoneChest.MODID, "entity/chest/" + path));
    }

    private static Material getChestMaterial(BlockEntityStoneChest entity, LocalChestRenderState renderState) {
        switch(renderState.type) {
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
