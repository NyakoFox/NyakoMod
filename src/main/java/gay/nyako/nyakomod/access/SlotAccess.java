package gay.nyako.nyakomod.access;

import net.minecraft.inventory.Inventory;

public interface SlotAccess {
    void setScale(int scale);
    int getScale();

    void setInventory(Inventory inventory);
}
