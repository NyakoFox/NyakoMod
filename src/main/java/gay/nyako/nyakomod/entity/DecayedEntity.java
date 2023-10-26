package gay.nyako.nyakomod.entity;

import gay.nyako.nyakomod.NyakoSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class DecayedEntity extends ZombieEntity {
    public DecayedEntity(EntityType<DecayedEntity> entityType, World world) {
        super(entityType, world);
    }

    public static boolean canSpawn(EntityType<net.minecraft.entity.mob.HuskEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return net.minecraft.entity.mob.HuskEntity.canSpawnInDark(type, world, spawnReason, pos, random) && (spawnReason == SpawnReason.SPAWNER || world.isSkyVisible(pos));
    }

    @Override
    protected boolean burnsInDaylight() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return NyakoSoundEvents.ENTITY_DECAYED_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return NyakoSoundEvents.ENTITY_DECAYED_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return NyakoSoundEvents.ENTITY_DECAYED_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return NyakoSoundEvents.ENTITY_DECAYED_STEP;
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean bl = super.tryAttack(target);
        if (bl && this.getMainHandStack().isEmpty() && target instanceof LivingEntity) {
            float f = this.getWorld().getLocalDifficulty(this.getBlockPos()).getLocalDifficulty();
            ((LivingEntity)target).addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 140 * (int)f), this);
        }
        return bl;
    }

    @Override
    protected boolean canConvertInWater() {
        return true;
    }

    @Override
    protected void convertInWater() {
        this.convertTo(EntityType.ZOMBIE);
        if (!this.isSilent()) {
            this.getWorld().syncWorldEvent(null, WorldEvents.HUSK_CONVERTS_TO_ZOMBIE, this.getBlockPos(), 0);
        }
    }

    @Override
    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }
}

