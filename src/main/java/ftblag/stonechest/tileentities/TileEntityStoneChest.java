package ftblag.stonechest.tileentities;

import ftblag.stonechest.StoneChest;
import ftblag.stonechest.blocks.EnumStoneChest;
import net.minecraft.tileentity.ChestTileEntity;

public class TileEntityStoneChest extends ChestTileEntity {

    private EnumStoneChest chestType;

    public TileEntityStoneChest() {
        this(EnumStoneChest.COBBLESTONE);
    }

    public TileEntityStoneChest(EnumStoneChest chestType) {
        super(StoneChest.chestTileType);
        this.chestType = chestType;
    }

    public EnumStoneChest getChestType() {
        return chestType;
    }
}
