package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.access.DragonFireballEntityAccess;
import gay.nyako.nyakomod.access.TntEntityAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(DragonFireballEntity.class)
public abstract class DragonFireballEntityMixin extends ExplosiveProjectileEntity implements DragonFireballEntityAccess {
    private boolean isFromAttack = false;

    protected DragonFireballEntityMixin(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void setFromAttack(boolean fromAttack) {
        this.isFromAttack = fromAttack;
    }

    @Override
    public boolean isFromAttack() {
        return this.isFromAttack;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putBoolean("isFromAttack", isFromAttack);
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        isFromAttack = nbt.getBoolean("isFromAttack");
        super.readCustomDataFromNbt(nbt);
    }

    /**
     * @author NyakoFox
     * @reason There's a lot more of them, should be able to hit
     */
    @Overwrite
    @Override
    public boolean canHit() {
        return isFromAttack;
    }

    /**
     * @author NyakoFox
     * @reason Restore default behavior
     */
    @Overwrite
    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!isFromAttack) {
            return false;
        }
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        this.scheduleVelocityUpdate();
        Entity entity = source.getAttacker();
        if (entity != null) {
            if (!this.world.isClient) {
                Vec3d vec3d = entity.getRotationVector();
                this.setVelocity(vec3d);
                this.powerX = vec3d.x * 0.1;
                this.powerY = vec3d.y * 0.1;
                this.powerZ = vec3d.z * 0.1;
                this.setOwner(entity);
            }
            return true;
        }
        return false;
    }

    @Redirect(method = "onCollision(Lnet/minecraft/util/hit/HitResult;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/AreaEffectCloudEntity;addEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)V"))
    private void injected(AreaEffectCloudEntity instance, StatusEffectInstance effect) {
        if (this.isFromAttack()) {
            instance.setParticleType(ParticleTypes.EXPLOSION);
            instance.setRadius(2.0f);
            instance.setDuration(20);
            instance.setRadiusGrowth(0);
            instance.setWaitTime(0);
            instance.addEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 20 * 10, 1));
        } else {
            instance.addEffect(effect);
        }
    }
}