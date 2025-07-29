package gay.nyako.nyakomod.entity;

import gay.nyako.nyakomod.NyakoEntities;
import gay.nyako.nyakomod.NyakoItems;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class BombEntity extends ThrownItemEntity {
    public BombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    public BombEntity(World world, LivingEntity owner) {
        super(NyakoEntities.BOMB, owner, world);
    }

    public BombEntity(EntityType<? extends BombEntity> entityType, LivingEntity livingEntity, World world) {
        super(entityType, livingEntity, world);
    }

    public BombEntity(World world, double x, double y, double z) {
        super(NyakoEntities.BOMB, x, y, z, world);
    }

    public BombEntity(EntityType<? extends BombEntity> entityType, double x, double y, double z, World world) {
        super(entityType, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return NyakoItems.BOMB;
    }

    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = this.getItem();
        if (itemStack.isEmpty())
        {
            itemStack = new ItemStack(getDefaultItem(), 1);
        }
        return new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack);
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            ParticleEffect particleEffect = this.getParticleParameters();

            for (int i = 0; i < 8; i++) {
                this.getWorld().addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        World world = this.getWorld();
        if (!world.isClient) {
            explode(world);
            world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            this.discard();
        }
    }

    protected void explode(World world) {
        world.createExplosion(this, this.getDamageSources().explosion(this, getOwner()), null, this.getPos(), 2.2F, false, World.ExplosionSourceType.TNT);
    }
}
