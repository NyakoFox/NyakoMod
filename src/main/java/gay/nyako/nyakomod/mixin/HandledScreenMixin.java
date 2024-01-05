package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.access.SlotAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
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

    @Redirect(method = "drawSlotHighlight(Lnet/minecraft/client/gui/DrawContext;III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fillGradient(Lnet/minecraft/client/render/RenderLayer;IIIIIII)V"))
    private static void injected(DrawContext context, RenderLayer layer, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, int z) {
        var width = endX - startX;
        var height = endY - startY;
        SlotAccess slot = (SlotAccess) ((HandledScreen) MinecraftClient.getInstance().currentScreen).focusedSlot;
        if (slot != null) {
            var scale = slot.getScale();
            width *= scale;
            height *= scale;
        }
        context.fillGradient(RenderLayer.getGuiOverlay(), startX, startY, startX + width, startY + height, colorStart, colorEnd, z);
    }

    @Redirect(method = "drawSlot(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/screen/slot/Slot;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawItem(Lnet/minecraft/item/ItemStack;III)V"))
    private void injected(DrawContext instance, ItemStack stack, int x, int y, int seed, DrawContext context, Slot slot) {
        var scale = ((SlotAccess) slot).getScale();

        var hadNBT = stack.hasNbt();
        var nbt = stack.getOrCreateNbt();
        nbt.putInt("renderScale", scale);
        context.drawItem(stack, x, y, seed);
        if (hadNBT) {
            nbt.remove("renderScale");
        } else {
            stack.setNbt(null);
        }
    }

    @Redirect(method = "drawSlot(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/screen/slot/Slot;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V"))
    private void injected(DrawContext instance, TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, DrawContext context, Slot slot) {
        var scale = ((SlotAccess) slot).getScale();

        var hadNBT = stack.hasNbt();
        var nbt = stack.getOrCreateNbt();
        nbt.putInt("renderScale", scale);
        context.drawItemInSlot(textRenderer, stack, x / scale, y / scale, countOverride);
        if (hadNBT) {
            nbt.remove("renderScale");
        } else {
            stack.setNbt(null);
        }
    }
}
