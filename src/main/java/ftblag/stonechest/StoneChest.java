package ftblag.stonechest;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

@Mod(StoneChest.MODID)
public class StoneChest {
    public static final String MODID = "stonechest";

    public StoneChest(IEventBus modEventBus) {
        SCRegistry.register(modEventBus);

        modEventBus.addListener((BuildCreativeModeTabContentsEvent e) -> {
            if (e.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
                SCRegistry.ITEMS.getEntries()
                        .stream()
                        .map(DeferredHolder::get)
                        .forEach(e::accept);
            }
        });
    }
}
