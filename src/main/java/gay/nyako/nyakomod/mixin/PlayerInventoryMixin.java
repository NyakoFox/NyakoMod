package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.CunkCoinUtils;
import gay.nyako.nyakomod.NyakoModItem;
import gay.nyako.nyakomod.item.BagOfCoinsItem;
import gay.nyako.nyakomod.item.CoinItem;
import gay.nyako.nyakomod.item.DevNullItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Final;
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

	@Shadow @Final public PlayerEntity player;

	@Shadow public abstract NbtList writeNbt(NbtList nbtList);

	@Inject(at = @At("HEAD"), method = "insertStack(ILnet/minecraft/item/ItemStack;)Z", cancellable = true)
	private void insertStack(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		var item = stack.getItem();
		if (item instanceof CoinItem) {
			var hungryBag = CunkCoinUtils.getHungryBag(player);

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

		var inventory = player.getInventory();

		for (int i = 0; i < inventory.size(); ++i) {
			var s = inventory.getStack(i);
			if (s.isOf(NyakoModItem.DEV_NULL_ITEM)) {
				var stored = DevNullItem.getStoredItem(s);
				if (stored != null && ItemStack.canCombine(stack, stored)) {
					var nbt = s.getNbt();
					nbt.putInt("stored_count", nbt.getInt("stored_count") + stack.getCount());
					stack.setCount(0);
					cir.setReturnValue(true);
					cir.cancel();
					break;
				}
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "addStack(ILnet/minecraft/item/ItemStack;)I", cancellable = true)
	private void addStackInjected(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		ItemStack itemStack = getStack(slot);
		var item = stack.getItem();

		if (item instanceof CoinItem) {
			if (!stack.isOf(NyakoModItem.NETHERITE_COIN_ITEM) &&
					(itemStack.getCount() + stack.getCount()) >= stack.getMaxCount()) {
				int left = (stack.getCount() + itemStack.getCount()) - stack.getMaxCount();

				Item type = NyakoModItem.COPPER_COIN_ITEM;
				if (stack.isOf(NyakoModItem.COPPER_COIN_ITEM)) {
					type = NyakoModItem.GOLD_COIN_ITEM;
				} else if (stack.isOf(NyakoModItem.GOLD_COIN_ITEM)) {
					type = NyakoModItem.EMERALD_COIN_ITEM;
				} else if (stack.isOf(NyakoModItem.EMERALD_COIN_ITEM)) {
					type = NyakoModItem.DIAMOND_COIN_ITEM;
				} else if (stack.isOf(NyakoModItem.DIAMOND_COIN_ITEM)) {
					type = NyakoModItem.NETHERITE_COIN_ITEM;
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
