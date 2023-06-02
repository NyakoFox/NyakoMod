package gay.nyako.nyakomod.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class ServerLivingEntityMixin  {
	@Inject(at = @At("HEAD"), method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V")
	private void onDeath(DamageSource source, CallbackInfo ci) {
		var entity = (LivingEntity)(Object)this;

		if (entity instanceof PlayerEntity) {
			return;
		}

		if (!entity.world.isClient() && entity.hasCustomName()) {
			var message = source.getDeathMessage(entity);
			var serverWorld = (ServerWorld)entity.world;
			var server = serverWorld.getServer();
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				player.sendMessage(message);
			}
		}
	}
}
