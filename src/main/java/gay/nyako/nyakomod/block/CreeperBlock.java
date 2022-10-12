package gay.nyako.nyakomod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;

public class CreeperBlock extends Block {
    public CreeperBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(HORIZONTAL_FACING, ctx.getPlayerFacing());
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
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        switch (state.get(HORIZONTAL_FACING)) {
            case NORTH, SOUTH -> {
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 1.125, 0.25, 0.75, 1.625, 0.75));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0.375, 0.375, 0.75, 1.125, 0.625));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5, 0, 0.625, 0.75, 0.375, 0.875));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0, 0.625, 0.5, 0.375, 0.875));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5, 0, 0.125, 0.75, 0.375, 0.375));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0, 0.125, 0.5, 0.375, 0.375));
            }
            case EAST, WEST -> {
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 1.125, 0.25, 0.75, 1.625, 0.75));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.375, 0.375, 0.25, 0.625, 1.125, 0.75));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.625, 0, 0.25, 0.875, 0.375, 0.5));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.625, 0, 0.5, 0.875, 0.375, 0.75));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0, 0.25, 0.375, 0.375, 0.5));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0, 0.5, 0.375, 0.375, 0.75));
            }
        }
        return shape;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }
}
