package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.item.BagOfCoinsItem;
import gay.nyako.nyakomod.item.CoinItem;
import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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

	public ItemStack getHungryBag() {
		var inventory = (PlayerInventory) (Object) this;
		for (int i = 0; i < inventory.size(); ++i) {
			var stack = inventory.getStack(i);
			if (stack.isOf(NyakoMod.HUNGRY_BAG_OF_COINS_ITEM)) {
        NbtCompound tag = stack.getOrCreateNbt();
				if (!tag.getBoolean("using")) {
					return stack;
				}
			}
		}

		return null;
	}

	@Inject(at = @At("HEAD"), method = "insertStack(ILnet/minecraft/item/ItemStack;)Z", cancellable = true)
	private void insertStack(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		var item = stack.getItem();
		if (item instanceof CoinItem) {
			var hungryBag = getHungryBag();

			if (hungryBag != null) {
				NbtCompound tag = hungryBag.getOrCreateNbt();
				var coin = (CoinItem)item;
				tag.putInt(coin.getCoinKey(), tag.getInt(coin.getCoinKey()) + stack.getCount());

				((BagOfCoinsItem) hungryBag.getItem()).rebalance(hungryBag);

				stack.setCount(0);
				cir.setReturnValue(true);
				cir.cancel();
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "addStack(ILnet/minecraft/item/ItemStack;)I", cancellable = true)
	private void addStackInjected(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		ItemStack itemStack = getStack(slot);
		var item = stack.getItem();

		if (item instanceof CoinItem) {
			if (!stack.isOf(NyakoMod.NETHERITE_COIN_ITEM) &&
					(itemStack.getCount() + stack.getCount()) >= stack.getMaxCount()) {
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
