package gay.nyako.nyakomod.mixin;

import java.util.Set;

import net.minecraft.network.packet.s2c.play.PositionFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gay.nyako.nyakomod.command.BackCommand;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TeleportCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Mixin(TeleportCommand.class)
public class TeleportCommandMixin {
  @Inject(at = @At("HEAD"), method = "teleport(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/entity/Entity;Lnet/minecraft/server/world/ServerWorld;DDDLjava/util/Set;FFLnet/minecraft/server/command/TeleportCommand$LookTarget;)V", cancellable=true)
  private static void teleport(ServerCommandSource source, Entity target, ServerWorld world, double x, double y, double z, Set<PositionFlag> movementFlags, float yaw, float pitch, TeleportCommand.LookTarget facingLocation, CallbackInfo ci) {
    if (target instanceof ServerPlayerEntity) {
      BackCommand.registerPreviousLocation((ServerPlayerEntity)target);
    }
  }
}
