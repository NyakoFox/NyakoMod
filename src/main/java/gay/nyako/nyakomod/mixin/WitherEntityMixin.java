package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoItems;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.access.LivingEntityAccess;
import gay.nyako.nyakomod.access.PlayerEntityAccess;
import gay.nyako.nyakomod.data.CunkCoinData;
import gay.nyako.nyakomod.utils.CunkCoinUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(WitherEntity.class)
public abstract class WitherEntityMixin extends HostileEntity {
	protected WitherEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	/**
	 * @author NyakoFox
	 * @reason Leaving out the nether star
	 */
	@Overwrite
	@Override
	public void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
		super.dropEquipment(source, lootingMultiplier, allowDrops);

		if (attackingPlayer != null && source.getSource() instanceof PlayerEntity && EnchantmentHelper.hasSilkTouch(attackingPlayer.getMainHandStack()))
		{
			ItemEntity itemEntity = dropItem(NyakoItems.WITHER);
			if (itemEntity != null) {
				itemEntity.setCovetedItem();
			}
			return;
		}

		ItemEntity itemEntity = this.dropItem(Items.NETHER_STAR);
		if (itemEntity != null) {
			itemEntity.setCovetedItem();
		}
	}
}
