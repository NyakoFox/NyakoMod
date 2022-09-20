package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.CunkCoinUtils;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.access.EntityAccess;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Shadow
	public PlayerEntity attackingPlayer;

	@Inject(at = @At("HEAD"), method = "dropLoot(Lnet/minecraft/entity/damage/DamageSource;Z)V")
	private void injected(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		if (!causedByPlayer) return;
		if (attackingPlayer == null) return;

		EntityType<?> type = this.getType();

		// get the amount of coins to give based off of the entity type
		int baseCoinAmount = CunkCoinUtils.getCoinValue(type);

		// pick a random number between 0.8 and 1.2
		double randomRange = ((Math.random() * (1.2 - 0.8)) + 0.8);

		// multiply the coin amount by that
		double coinAmount = baseCoinAmount * randomRange;

		if (source.isMagic()) coinAmount *= 1.05; // give a 5% bonus if magic is used
		if (source.isProjectile()) coinAmount *= 1.02; // give a 2% bonus if a projectile is used

		// get item in main hand
		ItemStack handStack = attackingPlayer.getMainHandStack();

		double bonus = 1;
		// 10% bonus for looting
		bonus += (0.10 * EnchantmentHelper.getLevel(Enchantments.LOOTING, handStack));
		// 2% bonus for fire aspect
		bonus += (0.02 * EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, handStack));
		// 2% bonus for flame
		bonus += (0.02 * EnchantmentHelper.getLevel(Enchantments.FLAME, handStack));

		coinAmount *= bonus;

		// if the player has luck, increase it by their luck level.
		// the luck status effect gives you +1 luck, so this should be 20% more coins if you have it...
		coinAmount += coinAmount * (0.2 * attackingPlayer.getLuck());

		// Increase the coin amount depending on the local difficulty
		// Minimum is a 0% increase, maximum is a 12% increase.
		// FORMULA: (localDifficulty / 6.75) * 0.12

		// RANGES:
		//              increase       difficulty
		// Peaceful:    0% - 0%        (0    -    0)
		// Easy:        1% - 3%        (0.75 -  1.5)
		// Normal:      3% - 7%        (1.5  -  4.0)
		// Hard:        4% - 12%       (2.25 - 6.75)

		float localDifficulty = world.getLocalDifficulty(getBlockPos()).getLocalDifficulty();
		coinAmount += coinAmount * (0.12 * (localDifficulty / 6.75));

		// drop less if the player has the curse of cunkless enchant
		if (EnchantmentHelper.getLevel(NyakoMod.CUNKLESS_CURSE_ENCHANTMENT, handStack) > 0) {
			coinAmount *= 0.5;
		}

		if (((EntityAccess)this).isFromSpawner()) {
			coinAmount *= 0.05; // Harshly drop if the entity was spawned from a spawner
		}

		// split the coin value we have into individual coin values
		Map<CunkCoinUtils.CoinValue, Integer> map = CunkCoinUtils.valueToSplit((int) coinAmount);

		Integer copper = map.get(CunkCoinUtils.CoinValue.COPPER);
		Integer gold = map.get(CunkCoinUtils.CoinValue.GOLD);
		Integer emerald = map.get(CunkCoinUtils.CoinValue.EMERALD);
		Integer diamond = map.get(CunkCoinUtils.CoinValue.DIAMOND);
		Integer netherite = map.get(CunkCoinUtils.CoinValue.NETHERITE);

		// drop the coins
		// this is super ugly but i didnt wanna make a function for some reason lol
		if (copper > 0) {
			ItemStack stack = new ItemStack(NyakoMod.COPPER_COIN_ITEM);
			stack.setCount(copper);
			dropStack(stack);
		}
		if (gold > 0) {
			ItemStack stack = new ItemStack(NyakoMod.GOLD_COIN_ITEM);
			stack.setCount(gold);
			dropStack(stack);
		}
		if (emerald > 0) {
			ItemStack stack = new ItemStack(NyakoMod.EMERALD_COIN_ITEM);
			stack.setCount(emerald);
			dropStack(stack);
		}
		if (diamond > 0) {
			ItemStack stack = new ItemStack(NyakoMod.DIAMOND_COIN_ITEM);
			stack.setCount(diamond);
			dropStack(stack);
		}
		if (netherite > 0) {
			ItemStack stack = new ItemStack(NyakoMod.NETHERITE_COIN_ITEM);
			stack.setCount(netherite);
			dropStack(stack);
		}
	}
}
