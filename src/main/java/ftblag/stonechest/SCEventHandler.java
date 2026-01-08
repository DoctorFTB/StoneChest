package ftblag.stonechest;

import ftblag.stonechest.client.BlockEntityStoneChestRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = StoneChest.MODID, value = Dist.CLIENT)
public class SCEventHandler {

    @SubscribeEvent
    public static void doClientStuff(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(SCRegistry.CHEST_BLOCK_ENTITY_TYPE.get(), BlockEntityStoneChestRenderer::new);
    }
}
