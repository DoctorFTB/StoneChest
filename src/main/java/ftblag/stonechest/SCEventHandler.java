package ftblag.stonechest;

import ftblag.stonechest.client.TileEntityStoneChestRenderer;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = StoneChest.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SCEventHandler {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void doClientStuff(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(SCRegistry.CHEST_TILE_TYPE.get(), TileEntityStoneChestRenderer::new);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(Sheets.CHEST_SHEET)) {
            for (Material material : TileEntityStoneChestRenderer.single) {
                event.addSprite(material.texture());
            }
            for (Material material : TileEntityStoneChestRenderer.left) {
                event.addSprite(material.texture());
            }
            for (Material material : TileEntityStoneChestRenderer.right) {
                event.addSprite(material.texture());
            }
        }
    }
}
