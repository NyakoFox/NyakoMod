package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
