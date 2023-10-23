package gay.nyako.nyakomod.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DrawContext.class)
public class ItemRendererMixin {

    @ModifyVariable(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At("HEAD"))
    private @Nullable String injected(@Nullable String countLabel, TextRenderer textRenderer, ItemStack stack) {
        if (stack.isEmpty() || (countLabel != null)) return null;

        if (stack.hasNbt() && stack.getNbt().contains("customCount")) {
            return stack.getNbt().getString("customCount");
        }
        return null;
    }
}
