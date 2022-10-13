package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoNetworking;
import gay.nyako.nyakomod.utils.InventoryUtils;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.Buffer;

@Mixin(value = ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(at = @At("HEAD"), method = "onClicked(Lnet/minecraft/item/ItemStack;Lnet/minecraft/screen/slot/Slot;Lnet/minecraft/util/ClickType;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/inventory/StackReference;)Z", cancellable = true)
    public void onClicked(ItemStack heldStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> cir) {
        var stack = (ItemStack)(Object)this;
        if (clickType == ClickType.RIGHT && stack.getCount() == 1) {
            if (stack.isOf(Items.ENDER_CHEST)) {
                InventoryUtils.openEnderChest(stack, player);
                cir.setReturnValue(true);
            } else if (stack.isOf(Items.SHULKER_BOX)) {
//                InventoryUtils.openShulkerBox(stack, player);
            }
        }
    }

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