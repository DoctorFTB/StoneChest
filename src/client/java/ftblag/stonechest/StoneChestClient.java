package ftblag.stonechest;

import ftblag.stonechest.client.BlockEntityStoneChestRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class StoneChestClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(SCRegistry.CHEST_BLOCK_ENTITY_TYPE, BlockEntityStoneChestRenderer::new);
    }
}
