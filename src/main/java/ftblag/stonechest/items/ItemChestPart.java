package ftblag.stonechest.items;

import ftblag.stonechest.StoneChest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemChestPart extends Item {

    public ItemChestPart(String name) {
        setCreativeTab(CreativeTabs.MATERIALS);
        setTranslationKey(name);
        setRegistryName(StoneChest.MODID, name);
    }
}
