package gay.nyako.nyakomod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.command.BackCommand;
import gay.nyako.nyakomod.command.XpCommand;
import gay.nyako.nyakomod.item.BagOfCoinsItem;
import gay.nyako.nyakomod.item.CoinItem;
import gay.nyako.nyakomod.struct.PlayerTeleportPayload;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
  @Inject(at = @At("HEAD"), method = "teleport(Lnet/minecraft/server/world/ServerWorld;DDDFF)V", cancellable = true)
  public void requestTeleport(ServerWorld world, double x1, double y1, double z1, float yaw1, float pitch1,
      CallbackInfo ci) {
    var p = (ServerPlayerEntity) (Object) this;

    BackCommand.registerPreviousLocation(p);
  }

  @Inject(at = @At("RETURN"), method = "teleport(Lnet/minecraft/server/world/ServerWorld;DDDFF)V", cancellable = true)
  public void requestTeleportReturn(ServerWorld world, double x1, double y1, double z1, float yaw1, float pitch1,
      CallbackInfo ci) {
    var p = (ServerPlayerEntity) (Object) this;

    XpCommand.refreshLevels(p);
  }

  @Inject(at = @At("TAIL"), method = "playerTick()V", cancellable = false)
  public void playerTick(CallbackInfo info) {
    var player = ((ServerPlayerEntity) (Object) this);

    var count = NyakoMod.countInventoryCoins(player.getInventory()) + NyakoMod.countInventoryCoins(player.getEnderChestInventory());

    player.getScoreboard().forEachScore(NyakoMod.COIN_CRITERIA, player.getEntityName(), score -> score.setScore(count));
  }
}
