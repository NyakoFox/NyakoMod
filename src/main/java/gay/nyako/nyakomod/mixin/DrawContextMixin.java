package gay.nyako.nyakomod.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DrawContext.class)
public class DrawContextMixin {

    @ModifyVariable(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At("HEAD"))
    private @Nullable String injected(@Nullable String countLabel, TextRenderer textRenderer, ItemStack stack) {
        if (stack.isEmpty() || (countLabel != null)) return null;

        if (stack.hasNbt() && stack.getNbt().contains("customCount")) {
            return stack.getNbt().getString("customCount");
        }
        return null;
    }

    @Redirect(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"))
    private void injected(MatrixStack instance, @Nullable LivingEntity entity, @Nullable World world, ItemStack stack, int x, int y, int seed, int z) {
        instance.push();
        if (stack.hasNbt() && stack.getNbt().contains("renderScale")) {
            var scale = stack.getNbt().getInt("renderScale");
            instance.translate(x, y, 0);
            instance.scale(scale, scale, 1f);
            instance.translate(-x, -y, 0);
        }
    }

    @Redirect(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"))
    public void injected(MatrixStack instance, TextRenderer textRenderer, ItemStack stack, int x, int y, @Nullable String countOverride) {
        instance.push();
        if (stack.hasNbt() && stack.getNbt().contains("renderScale")) {
            var scale = stack.getNbt().getInt("renderScale");
            //instance.translate(-x, -y, 0);
            instance.scale(scale, scale, 1f);
            //instance.translate(-x, -y, 0);
        }
    }
}
