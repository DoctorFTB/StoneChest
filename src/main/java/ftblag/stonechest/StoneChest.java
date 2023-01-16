package ftblag.stonechest;

import net.minecraftforge.fml.common.Mod;

@Mod(StoneChest.MODID)
public class StoneChest {
    public static final String MODID = "stonechest";

    public StoneChest() {
        SCRegistry.register();
    }
}
