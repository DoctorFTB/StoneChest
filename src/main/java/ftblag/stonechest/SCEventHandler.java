package ftblag.stonechest;

import ftblag.stonechest.client.TileEntityStoneChestRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = StoneChest.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SCEventHandler {

    @SubscribeEvent
    public static void doClientStuff(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(SCRegistry.CHEST_TILE_TYPE.get(), TileEntityStoneChestRenderer::new);
    }
}
