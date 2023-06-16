package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.access.SlotAccess;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Redirect(method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V"))
    public void injected(MatrixStack instance, double x, double y, double z, TextRenderer renderer, ItemStack stack) {
        instance.translate(x, y, z);
        if (stack.hasNbt() && stack.getNbt().contains("renderScale")) {
            var scale = stack.getNbt().getInt("renderScale");
            instance.translate(5, 2.375, 0);
            instance.scale(scale, scale, scale);
            instance.translate(-5 * scale, -2.375 * scale, 0);
        }
    }

    @Redirect(method = "renderGuiItemModel(Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"))
    private void injected(MatrixStack instance, ItemStack stack) {
        instance.push();
        if (stack.hasNbt() && stack.getNbt().contains("renderScale")) {
            var scale = stack.getNbt().getInt("renderScale");
            instance.translate(5, 2.375, 0);
            instance.scale(scale, scale, scale);
            instance.translate(-5 * scale, -2.375 * scale, 0);
        }
    }

    @Redirect(method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderGuiQuad(Lnet/minecraft/client/render/BufferBuilder;IIIIIIII)V"))
    private void injected(ItemRenderer instance, BufferBuilder buffer, int x, int y, int width, int height, int red, int green, int blue, int alpha, TextRenderer renderer, ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains("renderScale")) {
            var scale = stack.getNbt().getInt("renderScale");
            y *= scale;
            width *= scale;
            height *= scale;
        }

        instance.renderGuiQuad(buffer, x, y, width, height, red, green, blue, alpha);
    }

    @ModifyVariable(method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At("HEAD"))
    private @Nullable String injected(@Nullable String countLabel, TextRenderer textRenderer, ItemStack stack) {
        if (stack.isEmpty() || (countLabel != null)) return null;

        if (stack.hasNbt() && stack.getNbt().contains("customCount")) {
            return stack.getNbt().getString("customCount");
        }
        return null;
    }
}
