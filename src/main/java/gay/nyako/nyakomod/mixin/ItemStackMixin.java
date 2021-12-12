package gay.nyako.nyakomod.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Redirect(method="getTooltip(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipContext;)Ljava/util/List;", at=@At(value="INVOKE", target="Lnet/minecraft/text/Texts;setStyleIfAbsent(Lnet/minecraft/text/MutableText;Lnet/minecraft/text/Style;)Lnet/minecraft/text/MutableText;"))
    private MutableText redirect(MutableText text, Style style) {
        // Only use style if the text doesn't have a style...
        if (text.getStyle().isEmpty()) {
            // We don't actually want to use the default LORE_STYLE because it's ugly...
            Style style2 = Style.EMPTY.withColor(Formatting.GRAY);
            return text.setStyle(style2);
        }
        return text;
    }

    @Redirect(method="getTooltip(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipContext;)Ljava/util/List;", at=@At(value="INVOKE", target="Lnet/minecraft/item/ItemStack;hasCustomName()Z"))
    public boolean redirect(ItemStack stack) {
        // Never turn italic
        return false;
    }
}