package gay.nyako.nyakomod.block;

import gay.nyako.nyakomod.NyakoBlocks;
import gay.nyako.nyakomod.NyakoSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.sound.SoundCategory;
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
            entity.handleFallDamage(fallDistance, 0.0F, entity.getDamageSources().fall());
        }
    }

    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isClient()) {
            world.playSound(null, pos, NyakoSoundEvents.VENT, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }

        float vel = 0.8f;
        int amt = 1;

        BlockPos checkPos = pos.down();
        while (world.getBlockState(checkPos).isOf(NyakoBlocks.LAUNCHER))
        {
            amt++;
            checkPos = checkPos.down();
        }

        // Since this is setting the velocity, we don't want to set it to the amount of launchers,
        // since the entity will be launched that many times faster.
        vel = vel * amt;

        entity.setPos(pos.getX() + 0.5, pos.getY() + 1.0 , pos.getZ() + 0.5);
        entity.addVelocity(0,vel,0);

        super.onSteppedOn(world, pos, state, entity);
    }
}