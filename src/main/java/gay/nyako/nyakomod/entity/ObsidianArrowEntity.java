package gay.nyako.nyakomod.entity;

import gay.nyako.nyakomod.NyakoEntities;
import gay.nyako.nyakomod.NyakoItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ObsidianArrowEntity extends PersistentProjectileEntity {
    private final double damage = 4.0;

    public ObsidianArrowEntity(EntityType<ObsidianArrowEntity> entityType, World world) {
        super(entityType, world, new ItemStack(NyakoItems.OBSIDIAN_ARROW));
        setDamage(damage);
    }

    public ObsidianArrowEntity(World world, LivingEntity owner, ItemStack stack) {
        super(NyakoEntities.OBSIDIAN_ARROW, owner, world, stack);
        setDamage(damage);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(NyakoItems.OBSIDIAN_ARROW);
    }
}
