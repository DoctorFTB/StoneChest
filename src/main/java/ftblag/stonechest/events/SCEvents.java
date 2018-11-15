package ftblag.stonechest.events;

import ftblag.stonechest.StoneChest;
import ftblag.stonechest.client.CustomBakedModel;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = StoneChest.MODID, value = Side.CLIENT)
public class SCEvents {

    public static IBakedModel missingBaked = ModelLoaderRegistry.getMissingModel().bake(ModelLoaderRegistry.getMissingModel().getDefaultState(), DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModel(ModelBakeEvent e) {
        for (ModelResourceLocation mrl : e.getModelRegistry().getKeys()) {
            if (mrl.getNamespace().equals(StoneChest.MODID) && mrl.getPath().startsWith("chest_")) {
                if (mrl.getVariant().startsWith("facing")) {
                    e.getModelRegistry().putObject(mrl, new CustomBakedModel((PerspectiveMapWrapper) missingBaked, mrl.getPath()));
                }
            }
        }
    }
}
