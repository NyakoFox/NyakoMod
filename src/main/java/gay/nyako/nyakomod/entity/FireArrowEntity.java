package gay.nyako.nyakomod.entity;

import gay.nyako.nyakomod.NyakoEntities;
import gay.nyako.nyakomod.NyakoItems;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FireArrowEntity extends PersistentProjectileEntity {
    public FireArrowEntity(EntityType<FireArrowEntity> entityType, World world) {
        super(entityType, world, new ItemStack(NyakoItems.FIRE_ARROW));
        setOnFireFor(100);
    }

    public FireArrowEntity(World world, LivingEntity owner) {
        super(NyakoEntities.FIRE_ARROW, owner, world, new ItemStack(NyakoItems.FIRE_ARROW));
        setOnFireFor(100);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient && isOnFire()) {
            this.getWorld().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(NyakoItems.FIRE_ARROW);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        // Set the current block on fire
        World world = getWorld();
        Direction side = blockHitResult.getSide();
        BlockPos blockPos = blockHitResult.getBlockPos().offset(side);

        if (isOnFire() && AbstractFireBlock.canPlaceAt(world, blockPos, side))
        {
            world.setBlockState(blockPos, AbstractFireBlock.getState(world, blockPos));
        }

        super.onBlockHit(blockHitResult);
    }
}
