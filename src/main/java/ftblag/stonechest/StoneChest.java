package ftblag.stonechest;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

@Mod(StoneChest.MODID)
public class StoneChest {
    public static final String MODID = "stonechest";

    public StoneChest() {
        SCRegistry.register();

        FMLJavaModLoadingContext.get().getModEventBus().addListener((BuildCreativeModeTabContentsEvent e) -> {
            if (e.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
                SCRegistry.ITEMS.getEntries()
                        .stream()
                        .map(RegistryObject::get)
                        .forEach(e::accept);
            }
        });
    }
}
