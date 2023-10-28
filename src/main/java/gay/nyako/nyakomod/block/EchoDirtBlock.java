package gay.nyako.nyakomod.block;

import gay.nyako.nyakomod.NyakoBlocks;
import net.minecraft.block.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class EchoDirtBlock extends Block implements Fertilizable {
    public EchoDirtBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        if (!world.getBlockState(pos.up()).isTransparent(world, pos)) {
            return false;
        }
        for (BlockPos blockPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            if (world.getBlockState(blockPos).getBlock() != NyakoBlocks.ECHO_GROWTH) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        for (BlockPos blockPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.getBlock() == NyakoBlocks.ECHO_GROWTH) {
                world.setBlockState(pos, NyakoBlocks.ECHO_GROWTH.getDefaultState(), Block.NOTIFY_ALL);
            }
        }
    }
}
