package ftblag.stonechest;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(StoneChest.MODID)
public class StoneChest {
    public static final String MODID = "stonechest";

    public StoneChest(FMLJavaModLoadingContext ctx) {
        SCRegistry.register(ctx.getModEventBus());
    }
}
