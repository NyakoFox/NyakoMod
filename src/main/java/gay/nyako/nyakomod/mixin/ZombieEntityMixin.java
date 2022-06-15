package gay.nyako.nyakomod.mixin;

import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin {

    @Redirect(method= "onKilledOther(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)Z", at=@At(value="INVOKE", target="Lnet/minecraft/util/math/random/Random;nextBoolean()Z"))
    private boolean redirect(Random random) {
        return false;
    }
}