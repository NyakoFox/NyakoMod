package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.access.SlotAccess;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Slot.class)
public class SlotMixin implements SlotAccess {
    public int scale = 1;

    @Override
    public void setScale(int scale) {
        this.scale = scale;
    }

    @Override
    public int getScale() {
        return scale;
    }

    @Final
    @Shadow
    @Mutable
    public Inventory inventory;

    @Override
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
