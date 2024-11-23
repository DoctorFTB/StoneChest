package ftblag.stonechest;

import ftblag.stonechest.client.TEISRStoneChest;
import ftblag.stonechest.client.TileEntityStoneChestRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = StoneChest.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class SCEventHandler {

    @SubscribeEvent
    public static void doClientStuff(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(SCRegistry.CHEST_TILE_TYPE.get(), TileEntityStoneChestRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return TEISRStoneChest.INSTANCE;
            }
        }, SCRegistry.chestItems);
    }
}
