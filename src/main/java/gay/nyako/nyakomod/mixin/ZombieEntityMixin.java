package gay.nyako.nyakomod.mixin;

import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin {

    @Redirect(method= "onKilledOther(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)V", at=@At(value="INVOKE", target="Ljava/util/Random;nextBoolean()Z"))
    private boolean redirect(Random random) {
        return false;
    }
}