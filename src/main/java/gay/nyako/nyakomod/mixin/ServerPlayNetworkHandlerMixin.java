package gay.nyako.nyakomod.mixin;

import java.util.HashMap;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gay.nyako.nyakomod.command.BackCommand;
import gay.nyako.nyakomod.struct.PlayerTeleportPayload;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

  @Shadow
  public ServerPlayerEntity player;

  @Inject(at = @At("HEAD"), method = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;requestTeleport(DDDFFLjava/util/Set;Z)V", cancellable=true)
  public void requestTeleport(double x1, double y1, double z1, float yaw1, float pitch1, Set<PlayerPositionLookS2CPacket.Flag> flags, boolean shouldDismount, CallbackInfo ci) {
    System.out.println("Requesting Teleport");
    var p = player;


    if (((ServerPlayerEntityAccessor) player).isInNetherPortal()) {
      return;
    }

    if (player.getBlockStateAtPos().isOf(Blocks.END_PORTAL)) {
      System.out.println("In ender portal");
    }

    var payload = new PlayerTeleportPayload() {
      {
        player = p;
        x = p.getX();
        y = p.getY();
        z = p.getZ();
        yaw = p.getYaw();
        pitch = p.getPitch();
      }
    };

    int id = player.getId();

    if (BackCommand.previousLocations.containsKey(id)) {
      BackCommand.previousLocations.replace(id, payload);
    } else {
      BackCommand.previousLocations.put(id, payload);
    }
  }
}
