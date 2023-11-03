package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.access.PlayerEntityAccess;
import gay.nyako.nyakomod.utils.CunkCoinUtils;
import gay.nyako.nyakomod.access.ServerPlayerEntityAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.command.BackCommand;
import gay.nyako.nyakomod.command.XpCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements ServerPlayerEntityAccess {
    private boolean inSafeMode = false;
    private Vec3d joinPos = Vec3d.ZERO;
    private GameMode joinPreviousGameMode = GameMode.SURVIVAL;
    private GameMode joinGameMode = GameMode.SURVIVAL;

    @Inject(method = "copyFrom(Lnet/minecraft/server/network/ServerPlayerEntity;Z)V", at = @At("HEAD"))
    private void injected(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        ((PlayerEntityAccess) (PlayerEntity) (Object) this).setStickerPackCollection(((PlayerEntityAccess) oldPlayer).getStickerPackCollection());
    }

    @Override
    public void setSafeMode(boolean bool) {
        inSafeMode = bool;
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        ServerPlayerEntityAccess playerAccess = (ServerPlayerEntityAccess) player;
        if (inSafeMode) {
            playerAccess.setJoinPos(player.getPos());
            playerAccess.setJoinPreviousGameMode(player.interactionManager.getPreviousGameMode());
            playerAccess.setJoinGameMode(player.interactionManager.getGameMode());
            player.changeGameMode(GameMode.SPECTATOR);
        } else {
            var previous = playerAccess.getJoinPreviousGameMode();
            var current = playerAccess.getJoinGameMode();
            if (previous == null) previous = GameMode.SURVIVAL;
            if (current == null) current = GameMode.SURVIVAL;
            if (current == GameMode.SPECTATOR) current = previous;
            if (current == GameMode.SPECTATOR) current = GameMode.SURVIVAL;

            player.changeGameMode(previous);
            player.changeGameMode(current);
            player.refreshPositionAndAngles(
                    playerAccess.getJoinPos().getX(),
                    playerAccess.getJoinPos().getY(),
                    playerAccess.getJoinPos().getZ(),
                    player.getYaw(),
                    player.getPitch()
            );
            player.setVelocity(0, 0, 0);
        }
        player.refreshPositionAfterTeleport(playerAccess.getJoinPos());
    }

    @Override
    public boolean isInSafeMode() {
    return inSafeMode;
  }

    @Override
    public void setJoinPos(Vec3d pos) {
        joinPos = pos;
    }

    @Override
    public Vec3d getJoinPos() {
        return joinPos;
    }

    @Override
    public void setJoinPreviousGameMode(GameMode gameMode) {
        joinPreviousGameMode = gameMode;
    }

    @Override
    public GameMode getJoinPreviousGameMode() {
        return joinPreviousGameMode;
    }

    @Override
    public void setJoinGameMode(GameMode gameMode) {
        joinGameMode = gameMode;
    }

    @Override
    public GameMode getJoinGameMode() {
        return joinGameMode;
    }

    @Inject(at = @At("HEAD"), method = "teleport(Lnet/minecraft/server/world/ServerWorld;DDDFF)V", cancellable = true)
    public void requestTeleport(ServerWorld world, double x1, double y1, double z1, float yaw1, float pitch1, CallbackInfo ci) {
        var p = (ServerPlayerEntity) (Object) this;
        BackCommand.registerPreviousLocation(p);
    }

    @Inject(at = @At("RETURN"), method = "teleport(Lnet/minecraft/server/world/ServerWorld;DDDFF)V", cancellable = true)
    public void requestTeleportReturn(ServerWorld world, double x1, double y1, double z1, float yaw1, float pitch1, CallbackInfo ci) {
        var p = (ServerPlayerEntity) (Object) this;
        XpCommand.refreshLevels(p);
    }

    @Inject(at = @At("TAIL"), method = "playerTick()V", cancellable = false)
    public void playerTick(CallbackInfo info) {
        var player = ((ServerPlayerEntity) (Object) this);
        var count = CunkCoinUtils.countInventoryCoins(player.getInventory()) + CunkCoinUtils.countInventoryCoins(player.getEnderChestInventory());
        player.getScoreboard().forEachScore(NyakoMod.COIN_CRITERIA, player.getEntityName(), score -> score.setScore(count));
    }

    @Redirect(method = "trySleep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/DimensionType;natural()Z"))
    public boolean natural(DimensionType dimensionType) {
        var player = ((ServerPlayerEntity) (Object) this);
        if (player.getStackInHand(player.getActiveHand()).getItem() == Items.WATER_BUCKET)
        {
            return true;
        }
        return dimensionType.natural();
    }
}
