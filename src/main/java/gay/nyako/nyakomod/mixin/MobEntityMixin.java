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
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.UUID;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {
	@Inject(at = @At("HEAD"), method = "interactWithItem(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", cancellable = true)
	protected void interactWithItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		var itemStack = player.getStackInHand(hand);
		var entity = (LivingEntity)(Object)this;
		if (itemStack.isOf(NyakoItems.SOUL_JAR)) {
			var result = itemStack.getItem().useOnEntity(itemStack, player, entity, hand);
			if (result.isAccepted()) {
				cir.setReturnValue(result);
			}
		}
	}
}
