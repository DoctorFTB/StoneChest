package ftblag.stonechest.client;

import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LocalChestRenderState extends ChestRenderState {
    public net.minecraft.client.resources.model.Material customMaterial;
}
