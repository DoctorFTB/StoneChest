package ftblag.stonechest;

import net.fabricmc.api.ModInitializer;

public class StoneChest implements ModInitializer {
    public static final String MODID = "stonechest";

    @Override
    public void onInitialize() {
        SCRegistry.initialize();
    }
}
