package gay.nyako.nyakomod.mixin;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrindstoneScreenHandler.class)
public abstract class GrindstoneScreenHandlerMixin extends ScreenHandler {

    protected GrindstoneScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, Inventory input) {
        super(type, syncId);
        this.input = input;
    }

    @Shadow
    public final Inventory input;

    @ModifyVariable(method = "updateResult()V", at = @At("STORE"), ordinal = 2)
    private boolean injected(boolean x) {
        ItemStack itemStack = this.input.getStack(0);
        ItemStack itemStack2 = this.input.getStack(1);
        boolean bl4 = !itemStack.isEmpty() && !itemStack.isOf(Items.ENCHANTED_BOOK) && !itemStack.isOf(Items.ENCHANTED_GOLDEN_APPLE) && !itemStack.hasEnchantments() ||
                !itemStack2.isEmpty() && !itemStack2.isOf(Items.ENCHANTED_BOOK) && !itemStack2.isOf(Items.ENCHANTED_GOLDEN_APPLE) && !itemStack2.hasEnchantments();
        return bl4;
    }

    @Redirect(method = "updateResult()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getCount()I"))
    private int injected(ItemStack stack) {
        if (stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
            return 1;
        }
        return stack.getCount();
    }

    @Inject(method = "grind(Lnet/minecraft/item/ItemStack;II)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private void injected(ItemStack item, int damage, int amount, CallbackInfoReturnable<ItemStack> cir) {
        if (item.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
            cir.setReturnValue(new ItemStack(Items.GOLDEN_APPLE, amount * item.getCount()));
        }
    }
}
