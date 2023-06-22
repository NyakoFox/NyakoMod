package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoStatusEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class ClientEntityMixin {
    @Shadow
    public World world;

    public boolean isHunterGlowing = false;

    private static boolean shouldGlow(Entity entity) {
        if (!entity.world.isClient()) return false;

        if (!(entity instanceof LivingEntity)) return false;

        var player = MinecraftClient.getInstance().player;
        if (player == null) return false;

        return player.hasStatusEffect(NyakoStatusEffects.HUNTER_STATUS_EFFECT) && player.getBlockPos().isWithinDistance(entity.getBlockPos(), 32);
    }

    @Inject(at = @At("HEAD"), method = "isGlowing", cancellable = true)
    private void isGlowing(CallbackInfoReturnable<Boolean> ci) {
        var entity = (Entity) (Object) this;
        if (shouldGlow(entity)) {
            isHunterGlowing = true;
            ci.setReturnValue(true);
        } else {
            isHunterGlowing = false;
        }
    }

    @Inject(at = @At("HEAD"), method = "getTeamColorValue", cancellable = true)
    private void getTeamColorValue(CallbackInfoReturnable<Integer> ci) {
        if (isHunterGlowing) {
            var entity = (Entity) (Object) this;

            var team = entity.getScoreboardTeam();

            if (team == null) {
                if (entity instanceof PlayerEntity) {
                    ci.setReturnValue(0xFF00FF);
                } else if (entity instanceof HostileEntity) {
                    ci.setReturnValue(0xFF0000);
                } else if (entity instanceof PassiveEntity) {
                    ci.setReturnValue(0x00FF00);
                }
            }
        }
    }
}
