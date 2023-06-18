package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoItems;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {

    @Inject(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;", shift = At.Shift.AFTER), cancellable = true)
    private static void craft(ItemStack ingredient, ItemStack input, CallbackInfoReturnable<ItemStack> cir) {
        Potion potion = PotionUtil.getPotion(input);
        if (potion == Potions.AWKWARD && ingredient.isOf(NyakoItems.SPECULAR_FISH))
        {
            cir.setReturnValue(new ItemStack(NyakoItems.RECALL_POTION));
            cir.cancel();
        }
    }

    @Inject(method = "isValidIngredient(Lnet/minecraft/item/ItemStack;)Z", at = @At("RETURN"), cancellable = true)
    private static void injected(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || stack.isOf(NyakoItems.SPECULAR_FISH));
    }

    @Inject(method = "hasRecipe(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private static void injected(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        if (ingredient.isOf(NyakoItems.SPECULAR_FISH) && PotionUtil.getPotion(input) == Potions.AWKWARD) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
