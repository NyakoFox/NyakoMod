package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.inventory.ShulkerBoxInventory;
import gay.nyako.nyakomod.utils.InventoryUtils;
import gay.nyako.nyakomod.utils.ShulkerUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(at = @At("HEAD"), method = "onClicked(Lnet/minecraft/item/ItemStack;Lnet/minecraft/screen/slot/Slot;Lnet/minecraft/util/ClickType;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/inventory/StackReference;)Z", cancellable = true)
    public void onClicked(ItemStack heldStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> cir) {
        var stack = (ItemStack)(Object)this;
        if (clickType == ClickType.RIGHT && stack.getCount() == 1) {
            if (stack.isOf(Items.ENDER_CHEST)) {
                InventoryUtils.openEnderChest(slot, player);
                cir.setReturnValue(true);
            } else if (ShulkerUtils.IsShulkerBox(stack)) {
                InventoryUtils.openShulkerBox(slot, player);
                cir.setReturnValue(true);
            }
        }
    }
}