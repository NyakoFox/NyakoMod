package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.access.SlotAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HandledScreen.class)
public class HandledScreenMixin extends Screen {

    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Redirect(method = "isPointOverSlot(Lnet/minecraft/screen/slot/Slot;DD)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;isPointWithinBounds(IIIIDD)Z"))
    private boolean injected(HandledScreen<?> instance, int x, int y, int width, int height, double pointX, double pointY, Slot slot) {
        var scale = ((SlotAccess) slot).getScale();
        width *= scale;
        height *= scale;
        return instance.isPointWithinBounds(x, y, width, height, pointX, pointY);
    }

    @Redirect(method = "drawSlotHighlight(Lnet/minecraft/client/util/math/MatrixStack;III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;fillGradient(Lnet/minecraft/client/util/math/MatrixStack;IIIIIII)V"))
    private static void injected(MatrixStack matrixStack, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, int z) {
        var width = endX - startX;
        var height = endY - startY;
        SlotAccess slot = (SlotAccess) ((HandledScreen) MinecraftClient.getInstance().currentScreen).focusedSlot;
        if (slot != null) {
            var scale = slot.getScale();
            width *= scale;
            height *= scale;
        }
        HandledScreen.fillGradient(matrixStack, startX, startY, startX + width, startY + height, colorStart, colorEnd, z);
    }

    @Redirect(method = "drawSlot(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/screen/slot/Slot;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V"))
    private void injected(ItemRenderer instance, TextRenderer renderer, ItemStack stack, int x, int y, String countLabel, MatrixStack matrices, Slot slot) {
        var scale = ((SlotAccess) slot).getScale();

        var hadNBT = stack.hasNbt();
        var nbt = stack.getOrCreateNbt();
        nbt.putInt("renderScale", scale);
        this.itemRenderer.renderGuiItemOverlay(this.textRenderer, stack, x, y, countLabel);
        if (hadNBT) {
            nbt.remove("renderScale");
        } else {
            stack.setNbt(null);
        }
    }

    @Redirect(method = "drawSlot(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/screen/slot/Slot;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderInGuiWithOverrides(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;III)V"))
    private void injected(ItemRenderer instance, LivingEntity entity, ItemStack stack, int x, int y, int seed, MatrixStack matrices, Slot slot) {
        var scale = ((SlotAccess) slot).getScale();

        var hadNBT = stack.hasNbt();
        var nbt = stack.getOrCreateNbt();
        nbt.putInt("renderScale", scale);
        itemRenderer.renderInGuiWithOverrides(entity, stack, x, y, seed);
        if (hadNBT) {
            nbt.remove("renderScale");
        } else {
            stack.setNbt(null);
        }
    }
}
