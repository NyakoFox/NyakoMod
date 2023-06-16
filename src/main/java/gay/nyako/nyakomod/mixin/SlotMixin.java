package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.access.SlotAccess;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;

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
}
