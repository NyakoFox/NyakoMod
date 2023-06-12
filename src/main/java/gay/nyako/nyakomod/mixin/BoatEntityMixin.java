package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.access.BoatEntityAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity implements BoatEntityAccess {
    private boolean pressingJump = false;

    @Shadow
    protected abstract boolean checkBoatInWater();

    public BoatEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void setPressingJump(boolean isPressingJump) {
        pressingJump = isPressingJump;
    }

    @Override
    public boolean isPressingJump() {
        return pressingJump;
    }

    @Inject(method = "updatePaddles()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V", shift = At.Shift.AFTER))
    private void injected(CallbackInfo ci) {
        if (((BoatEntityAccess) this).isPressingJump() && (this.isOnGround() || this.checkBoatInWater()))
        {
            Vec3d velocity = this.getVelocity();
            this.setVelocity(velocity.x, 0.35, velocity.z);
        }
    }
}
