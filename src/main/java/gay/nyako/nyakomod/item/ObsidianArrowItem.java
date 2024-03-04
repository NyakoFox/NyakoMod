package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.entity.ObsidianArrowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ObsidianArrowItem extends ArrowItem {
    public ObsidianArrowItem(Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new ObsidianArrowEntity(world, shooter, stack);
    }
}
