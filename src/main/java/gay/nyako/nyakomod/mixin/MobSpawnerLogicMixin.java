package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.access.EntityAccess;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.MobSpawnerLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MobSpawnerLogic.class)
public abstract class MobSpawnerLogicMixin {

    @Redirect(method= "serverTick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V", at=@At(value="INVOKE", target="Lnet/minecraft/server/world/ServerWorld;spawnNewEntityAndPassengers(Lnet/minecraft/entity/Entity;)Z"))
    public boolean spawnNewEntityAndPassengers(ServerWorld world, Entity entity) {
        ((EntityAccess)entity).setFromSpawner(true);
        return world.spawnNewEntityAndPassengers(entity);
    }
}