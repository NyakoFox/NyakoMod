package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.access.PlayerEntityAccess;
import io.github.tropheusj.milk.Milk;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Milk.class)
public abstract class MilkMixin {

    @Inject(method = "tryRemoveRandomEffect(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private static void injected(LivingEntity user, CallbackInfoReturnable<Boolean> cir) {
        if (user instanceof PlayerEntity player) {
            var access = (PlayerEntityAccess) user;
            if (access.getMilk() < 20) {
                access.addMilk(2);

                if (!user.getWorld().isClient()) {
                    player.getScoreboard().forEachScore(NyakoMod.MILK_CONSUMED_CRITERIA, player.getEntityName(), score -> score.setScore(score.getScore() + 1));
                }

                cir.setReturnValue(true);
            }
        }
    }
}
