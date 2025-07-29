package gay.nyako.nyakomod.entity;

import gay.nyako.nyakomod.NyakoEntities;
import gay.nyako.nyakomod.NyakoItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class GrenadeEntity extends BombEntity {
    public GrenadeEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    public GrenadeEntity(World world, LivingEntity owner) {
        super(NyakoEntities.GRENADE, owner, world);
    }

    public GrenadeEntity(World world, double x, double y, double z) {
        super(NyakoEntities.GRENADE, x, y, z, world);
    }

    @Override
    protected void explode(World world) {
        world.createExplosion(this, this.getDamageSources().explosion(this, getOwner()), null, this.getPos(), 1.25F, false, World.ExplosionSourceType.NONE);
    }

    @Override
    protected Item getDefaultItem() {
        return NyakoItems.GRENADE;
    }
}
