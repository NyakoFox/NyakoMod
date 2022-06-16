package gay.nyako.nyakomod.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Redirect(method="renderHeldItemTooltip(Lnet/minecraft/client/util/math/MatrixStack;)V", at=@At(value="INVOKE", target="Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;"))
    public MutableText redirect(MutableText instance, Formatting formatting) {
        return instance;
    }
}