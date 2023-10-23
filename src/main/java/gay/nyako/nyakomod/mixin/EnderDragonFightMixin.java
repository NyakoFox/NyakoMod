package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.EndPortalFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderDragonFight.class)
public abstract class EnderDragonFightMixin {
	@Shadow private boolean previouslyKilled;
	@Shadow @Final private ServerWorld world;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/dragon/EnderDragonFight;generateNewEndGateway()V"),
			method = "dragonKilled(Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;)V"
	)
	protected void dragonKilled(EnderDragonEntity dragon, CallbackInfo ci) {
		if (this.previouslyKilled) {
			this.world.setBlockState(dragon.getWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING, BlockPos.ORIGIN), Blocks.DRAGON_EGG.getDefaultState());
		}
	}
}
