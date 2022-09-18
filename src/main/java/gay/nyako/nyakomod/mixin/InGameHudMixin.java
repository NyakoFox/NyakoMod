package gay.nyako.nyakomod.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Redirect(method="renderHeldItemTooltip(Lnet/minecraft/client/util/math/MatrixStack;)V", at=@At(value="INVOKE", target="Lnet/minecraft/item/ItemStack;hasCustomName()Z"))
    public boolean redirect(ItemStack stack) {
        // Never turn italic
        return false;
    }
}