package ftblag.stonechest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.math.Transformation;
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
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.Locale;
import java.util.Map;

public class BlockEntityStoneChestRenderer implements BlockEntityRenderer<BlockEntityStoneChest, LocalChestRenderState> {
	private static final Map<Direction, Transformation> TRANSFORMATIONS = Util.makeEnumMap(Direction.class, BlockEntityStoneChestRenderer::createModelTransformation);

    public static SpriteId[] single = new SpriteId[EnumStoneChest.VALUES.length];
    public static SpriteId[] left = new SpriteId[EnumStoneChest.VALUES.length];
    public static SpriteId[] right = new SpriteId[EnumStoneChest.VALUES.length];

    static {
        for (EnumStoneChest type : EnumStoneChest.VALUES) {
            single[type.ordinal()] = getChestSpriteId(type.name().toLowerCase(Locale.ENGLISH));
            left[type.ordinal()] = getChestSpriteId(type.name().toLowerCase(Locale.ENGLISH) + "_left");
            right[type.ordinal()] = getChestSpriteId(type.name().toLowerCase(Locale.ENGLISH) + "_right");
        }
    }

    private final SpriteGetter sprites;
    private final ChestModel singleModel;
    private final ChestModel doubleLeftModel;
    private final ChestModel doubleRightModel;

    public BlockEntityStoneChestRenderer(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
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
        renderState.facing = blockstate.getValue(ChestBlock.FACING);
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

        renderState.customSprite = getChestSpriteId(blockEntity, renderState);
    }

    @Override
    public void submit(LocalChestRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.mulPose(modelTransformation(renderState.facing));

        float f = renderState.open;
        f = 1.0F - f;
        f = 1.0F - f * f * f;
        SpriteId spriteId = renderState.customSprite;
        ChestModel model;

        if (renderState.type != ChestType.SINGLE) {
            if (renderState.type == ChestType.LEFT) {
                model = this.doubleLeftModel;
            } else {
                model = this.doubleRightModel;
            }
        } else {
            model = this.singleModel;
        }

        nodeCollector.submitModel(
                model,
                f,
                poseStack,
                renderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                -1,
                spriteId,
                this.sprites,
                0,
                renderState.breakProgress
        );

        poseStack.popPose();
    }

    private static SpriteId getChestSpriteId(String path) {
        return new SpriteId(Sheets.CHEST_SHEET, Identifier.fromNamespaceAndPath(StoneChest.MODID, "entity/chest/" + path));
    }

    private static SpriteId getChestSpriteId(BlockEntityStoneChest entity, LocalChestRenderState renderState) {
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

	public static Transformation modelTransformation(Direction facing) {
		return TRANSFORMATIONS.get(facing);
	}

	private static Transformation createModelTransformation(Direction facing) {
		return new Transformation(new Matrix4f().rotationAround(Axis.YP.rotationDegrees(-facing.toYRot()), 0.5F, 0.0F, 0.5F));
	}
}
