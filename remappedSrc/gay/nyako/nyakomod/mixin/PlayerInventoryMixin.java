package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.utils.CunkCoinUtils;
import gay.nyako.nyakomod.NyakoItems;
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
		var inventory = player.getInventory();

		for (int i = 0; i < inventory.size(); ++i) {
			var s = inventory.getStack(i);
			if (s.isOf(NyakoItems.ENCUMBERING_STONE)) {
				var nbt = s.getNbt();
				if (nbt == null || !nbt.contains("locked") || nbt.getBoolean("locked")) {
					cir.setReturnValue(false);
					cir.cancel();
				}
			} else if (s.isOf(NyakoItems.SUPER_ENCUMBERING_STONE)) {
				cir.setReturnValue(false);
				cir.cancel();
			}
		}

		for (int i = 0; i < inventory.size(); ++i) {
			var s = inventory.getStack(i);
			if (s.isOf(NyakoItems.DEV_NULL)) {
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
	}

	@Inject(at = @At("HEAD"), method = "addStack(ILnet/minecraft/item/ItemStack;)I", cancellable = true)
	private void addStackInjected(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		ItemStack itemStack = getStack(slot);
		var item = stack.getItem();

		if (item instanceof CoinItem) {
			if (!stack.isOf(NyakoItems.NETHERITE_COIN) &&
					(itemStack.getCount() + stack.getCount()) >= stack.getMaxCount()) {
				int left = (stack.getCount() + itemStack.getCount()) - stack.getMaxCount();

				Item type = NyakoItems.COPPER_COIN;
				if (stack.isOf(NyakoItems.COPPER_COIN)) {
					type = NyakoItems.GOLD_COIN;
				} else if (stack.isOf(NyakoItems.GOLD_COIN)) {
					type = NyakoItems.EMERALD_COIN;
				} else if (stack.isOf(NyakoItems.EMERALD_COIN)) {
					type = NyakoItems.DIAMOND_COIN;
				} else if (stack.isOf(NyakoItems.DIAMOND_COIN)) {
					type = NyakoItems.NETHERITE_COIN;
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
