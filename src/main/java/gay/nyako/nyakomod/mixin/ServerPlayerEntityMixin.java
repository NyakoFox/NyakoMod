package gay.nyako.nyakomod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gay.nyako.nyakomod.command.BackCommand;
import gay.nyako.nyakomod.struct.PlayerTeleportPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
  @Inject(at = @At("HEAD"), method = "Lnet/minecraft/server/network/ServerPlayerEntity;teleport(Lnet/minecraft/server/world/ServerWorld;DDDFF)V", cancellable=true)
  public void requestTeleport(ServerWorld world, double x1, double y1, double z1, float yaw1, float pitch1, CallbackInfo ci) {
    var p = (ServerPlayerEntity) (Object) this;

    BackCommand.registerPreviousLocation(p);
  }
}
