package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.access.EntityAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SlimeEntity.class)
public abstract class SlimeEntityMixin extends LivingEntity {
	public SlimeEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "remove(Lnet/minecraft/entity/Entity$RemovalReason;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/SlimeEntity;refreshPositionAndAngles(DDDFF)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void injected(RemovalReason reason, CallbackInfo ci, int i, Text text, boolean bl, float f, int j, int k, int l, float g, float h, SlimeEntity slimeEntity) {
		boolean fromSpawner = ((EntityAccess)this).isFromSpawner();
		((EntityAccess)slimeEntity).setFromSpawner(fromSpawner);
	}
}
