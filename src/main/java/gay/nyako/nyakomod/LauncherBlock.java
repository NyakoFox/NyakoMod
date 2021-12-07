package gay.nyako.nyakomod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LauncherBlock extends Block {

    public LauncherBlock(Settings settings) {
        super(settings);
    }

    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (entity.bypassesLandingEffects()) {
            super.onLandedUpon(world, state, pos, entity, fallDistance);
        } else {
            entity.handleFallDamage(fallDistance, 0.0F, DamageSource.FALL);
        }
    }

    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        entity.addVelocity(0,5,0);

        super.onSteppedOn(world, pos, state, entity);
    }
}