package gay.nyako.nyakomod.entity;

import gay.nyako.nyakomod.NyakoEntities;
import gay.nyako.nyakomod.NyakoItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class SafetyBombEntity extends BombEntity {
    public SafetyBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    public SafetyBombEntity(World world, LivingEntity owner) {
        super(NyakoEntities.SAFETY_BOMB, owner, world);
    }

    public SafetyBombEntity(World world, double x, double y, double z) {
        super(NyakoEntities.SAFETY_BOMB, x, y, z, world);
    }

    @Override
    protected void explode(World world) {
        world.createExplosion(this, this.getDamageSources().explosion(this, getOwner()), null, this.getPos(), 2.2F, false, World.ExplosionSourceType.NONE);
    }

    @Override
    protected Item getDefaultItem() {
        return NyakoItems.SAFETY_BOMB;
    }
}
