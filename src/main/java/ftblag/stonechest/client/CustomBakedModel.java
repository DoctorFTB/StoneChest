package ftblag.stonechest.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

public class CustomBakedModel implements IBakedModel {

    private PerspectiveMapWrapper wrapper;
    private TextureAtlasSprite sprite;

    public CustomBakedModel(PerspectiveMapWrapper wrapper, String name) {
        this.wrapper = wrapper;
        sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(getName(name));
    }

    private static String getName(String name) {
        name = name.substring("chest_".length());
        if (name.endsWith("ite"))
            name = "stone_" + name;
        return "minecraft:blocks/" + name;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return wrapper.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return wrapper.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return wrapper.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return wrapper.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return sprite;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return wrapper.getOverrides();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return wrapper.getItemCameraTransforms();
    }

    @Override
    public boolean isAmbientOcclusion(IBlockState state) {
        return wrapper.isAmbientOcclusion(state);
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return wrapper.handlePerspective(cameraTransformType);
    }
}
