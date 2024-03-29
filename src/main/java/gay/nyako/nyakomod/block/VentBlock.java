package gay.nyako.nyakomod.block;

import gay.nyako.nyakomod.NyakoBlocks;
import gay.nyako.nyakomod.NyakoSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;

public class VentBlock extends Block {

    public VentBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
                this.stateManager.getDefaultState()
                        .with(HORIZONTAL_FACING, Direction.NORTH)
        );
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(HORIZONTAL_FACING, rotation.rotate(state.get(HORIZONTAL_FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(HORIZONTAL_FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (entity.bypassesLandingEffects()) {
            super.onLandedUpon(world, state, pos, entity, fallDistance);
        } else {
            entity.handleFallDamage(fallDistance, 0.0F, entity.getDamageSources().fall());
        }
    }

    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        double x = state.get(HORIZONTAL_FACING).getOffsetX();
        double z = state.get(HORIZONTAL_FACING).getOffsetZ();

        double horizontalSpeed = 1.5;
        double verticalSpeed = 0.5;

        if (!world.isClient()) {
            world.playSound(null, pos, NyakoSoundEvents.VENT, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }

        int amt = 1;

        BlockPos checkPos = pos.down();
        while (world.getBlockState(checkPos).isOf(NyakoBlocks.LAUNCHER))
        {
            amt++;
            checkPos = checkPos.down();
        }

        // Since this is setting the velocity, we don't want to set it to the amount of launchers,
        // since the entity will be launched that many times faster.
        verticalSpeed = verticalSpeed * amt;

        entity.setPos(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
        entity.setVelocity(x * horizontalSpeed, verticalSpeed, z * horizontalSpeed);

        super.onSteppedOn(world, pos, state, entity);
    }
}