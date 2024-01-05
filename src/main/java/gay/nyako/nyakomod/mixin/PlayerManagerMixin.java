package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.access.PlayerEntityAccess;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Inject(method = "respawnPlayer",
            at = @At(
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;setHealth(F)V",
                    value = "INVOKE",
                    ordinal = 0
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void respawnPlayer(ServerPlayerEntity player, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir, BlockPos blockPos, float f, boolean bl, ServerWorld serverWorld, Optional optional, ServerWorld serverWorld2, ServerPlayerEntity serverPlayerEntity) {
        var manager = player.hungerManager;

        if (manager.getFoodLevel() < 8) {
            manager.setFoodLevel(8);
        }

        manager.setExhaustion(0);
        manager.setSaturationLevel(3);

        serverPlayerEntity.hungerManager = player.hungerManager;
    }
}
