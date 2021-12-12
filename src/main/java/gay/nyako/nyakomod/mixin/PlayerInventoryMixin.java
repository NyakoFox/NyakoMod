package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

	@Shadow
	public abstract int addStack(ItemStack stack);

	@Shadow
	public abstract int addStack(int slot, ItemStack stack);

	@Shadow
	public abstract ItemStack getStack(int slot);

	@Shadow
	public abstract void setStack(int slot, ItemStack stack);

	@Inject(at = @At("HEAD"), method = "addStack(ILnet/minecraft/item/ItemStack;)I", cancellable=true)
	private void injected(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (stack.isOf(NyakoMod.COPPER_COIN_ITEM) ||
			stack.isOf(NyakoMod.GOLD_COIN_ITEM) ||
			stack.isOf(NyakoMod.EMERALD_COIN_ITEM) ||
			stack.isOf(NyakoMod.DIAMOND_COIN_ITEM)) {
			ItemStack itemStack = getStack(slot);
			if ((itemStack.getCount() + stack.getCount()) >= stack.getMaxCount()) {
				int left = (stack.getCount() + itemStack.getCount()) - stack.getMaxCount();

				Item type = NyakoMod.COPPER_COIN_ITEM;
				if (stack.isOf(NyakoMod.COPPER_COIN_ITEM)) {
					type = NyakoMod.GOLD_COIN_ITEM;
				} else if (stack.isOf(NyakoMod.GOLD_COIN_ITEM)) {
					type = NyakoMod.EMERALD_COIN_ITEM;
				} else if (stack.isOf(NyakoMod.EMERALD_COIN_ITEM)) {
					type = NyakoMod.DIAMOND_COIN_ITEM;
				} else if (stack.isOf(NyakoMod.DIAMOND_COIN_ITEM)) {
					type = NyakoMod.NETHERITE_COIN_ITEM;
				}

				setStack(slot, ItemStack.EMPTY);

				ItemStack newStack = new ItemStack(type);
				newStack.setCount(1);
				addStack(newStack);

				if (left > 0) {
					stack.setCount(left);
					addStack(stack);
				}

				cir.cancel();
			}
		}
	}
}
