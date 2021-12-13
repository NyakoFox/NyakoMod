package gay.nyako.nyakomod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gay.nyako.nyakomod.BagOfCoinsItem;
import gay.nyako.nyakomod.CoinItem;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.command.BackCommand;
import gay.nyako.nyakomod.struct.PlayerTeleportPayload;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
  @Inject(at = @At("HEAD"), method = "Lnet/minecraft/server/network/ServerPlayerEntity;teleport(Lnet/minecraft/server/world/ServerWorld;DDDFF)V", cancellable = true)
  public void requestTeleport(ServerWorld world, double x1, double y1, double z1, float yaw1, float pitch1,
      CallbackInfo ci) {
    var p = (ServerPlayerEntity) (Object) this;

    BackCommand.registerPreviousLocation(p);
  }

  @Inject(at = @At("TAIL"), method = "Lnet/minecraft/server/network/ServerPlayerEntity;playerTick()V", cancellable = false)
  public void playerTick(CallbackInfo info) {
    var player = ((ServerPlayerEntity) (Object) this);

    var count = countInventoryCoins(player.getInventory()) + countInventoryCoins(player.getEnderChestInventory());

    player.getScoreboard().forEachScore(NyakoMod.COIN_CRITERIA, player.getEntityName(), score -> score.setScore(count));
  }

  public int countInventoryCoins(Inventory inventory) {
    int total = 0;
    for (int i = 0; i < inventory.size(); ++i) {
      var stack = inventory.getStack(i);
      var item = stack.getItem();

      if (item instanceof CoinItem) {
        total += stack.getCount() * ((CoinItem) item).getCoinValue();
      } else if (item instanceof BagOfCoinsItem) {
        NbtCompound tag = stack.getOrCreateNbt();
        total += tag.getInt("copper");
        total += tag.getInt("gold") * 100;
        total += tag.getInt("emerald") * 10000;
        total += tag.getInt("diamond") * 1000000;
        total += tag.getInt("netherite") * 100000000;
      }
    }

    return total;
  }
}
