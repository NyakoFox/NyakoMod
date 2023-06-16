package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoItems;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.access.EntityAccess;
import gay.nyako.nyakomod.access.PlayerEntityAccess;
import gay.nyako.nyakomod.utils.CunkCoinUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	private static final UUID MILK_BOOST_ID = UUID.fromString("4cfe098c-ef25-462e-8c06-41964634ab1a");
	private static final UUID MILK_ATTACK_SPEED_ID = UUID.fromString("8959b2d5-086d-4d00-8fb0-e7cd7bc75342");
	private static final EntityAttributeModifier MILK_BOOST = new EntityAttributeModifier(MILK_BOOST_ID, "Milk speed boost", 0.2f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
	private static final EntityAttributeModifier MILK_ATTACK_SPEED = new EntityAttributeModifier(MILK_ATTACK_SPEED_ID, "Milk attack speed", 0.4f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

	@Shadow
	public abstract EntityAttributeInstance getAttributeInstance(EntityAttribute attribute);

	@Shadow
	protected PlayerEntity attackingPlayer;

	@Shadow public abstract ItemStack getStackInHand(Hand hand);

	@Shadow public abstract void endCombat();

	@Inject(at = @At("HEAD"), method = "dropLoot(Lnet/minecraft/entity/damage/DamageSource;Z)V")
	private void injected(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		if (!causedByPlayer) return;
		if (attackingPlayer == null) return;

		EntityType<?> type = this.getType();

		// get the amount of coins to give based off of the entity type
		double baseCoinAmount = CunkCoinUtils.getCoinValue(type);

		if (type == EntityType.SLIME || type == EntityType.MAGMA_CUBE) {
			SlimeEntity slime = (SlimeEntity) (Object) this;
			baseCoinAmount *= (slime.getSize() / 5d);
		}

		if (type == EntityType.ENDERMAN) {
			if (this.world.getRegistryKey() == World.END) {
				baseCoinAmount *= 0.1;
			} else if (this.world.getRegistryKey() == World.NETHER) {
				baseCoinAmount *= 0.5;
			}

			if (this.world.getRegistryKey() != World.END) {
				if (random.nextBetween(1, 500) == 1) {
					this.dropItem(NyakoItems.ROD_OF_DISCORD);
				}
			}
		}

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

		// drop coins
		if (copper    > 0) dropStack(new ItemStack(NyakoItems.COPPER_COIN,    copper));
		if (gold      > 0) dropStack(new ItemStack(NyakoItems.GOLD_COIN,      gold));
		if (emerald   > 0) dropStack(new ItemStack(NyakoItems.EMERALD_COIN,   emerald));
		if (diamond   > 0) dropStack(new ItemStack(NyakoItems.DIAMOND_COIN,   diamond));
		if (netherite > 0) dropStack(new ItemStack(NyakoItems.NETHERITE_COIN, netherite));
	}

	@Inject(at = @At("HEAD"), method = "applyMovementEffects(Lnet/minecraft/util/math/BlockPos;)V")
	private void applyMovementEffects(BlockPos pos, CallbackInfo ci) {
		var entity = (LivingEntity)(Object)this;

		if (!(entity instanceof PlayerEntity player)) {
			return;
		}

		if (player.world.isClient()) {
			return;
		}

		EntityAttributeInstance entityAttributeSpeedInstance = getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
		if (entityAttributeSpeedInstance == null) {
			return;
		}
		EntityAttributeInstance entityAttributeAttackSpeedInstance = getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED);
		if (entityAttributeAttackSpeedInstance == null) {
			return;
		}

		var playerAccess = (PlayerEntityAccess)player;

		if (playerAccess.getMilk() >= 18) {
			if (!entityAttributeSpeedInstance.hasModifier(MILK_BOOST)) {
				entityAttributeSpeedInstance.addTemporaryModifier(MILK_BOOST);
			}
			if (!entityAttributeAttackSpeedInstance.hasModifier(MILK_ATTACK_SPEED)) {
				entityAttributeAttackSpeedInstance.addTemporaryModifier(MILK_ATTACK_SPEED);
			}
		} else {
			if (entityAttributeSpeedInstance.hasModifier(MILK_BOOST)) {
				entityAttributeSpeedInstance.removeModifier(MILK_BOOST);
			}
			if (entityAttributeAttackSpeedInstance.hasModifier(MILK_ATTACK_SPEED)) {
				entityAttributeAttackSpeedInstance.removeModifier(MILK_ATTACK_SPEED);
			}
		}
	}
}
