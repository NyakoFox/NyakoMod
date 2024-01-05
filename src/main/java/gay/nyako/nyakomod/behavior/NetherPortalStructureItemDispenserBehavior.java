package gay.nyako.nyakomod.behavior;

import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.FluidModificationItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class NetherPortalStructureItemDispenserBehavior extends ItemDispenserBehavior {

    @Override
    public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        BlockPos origin = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
        ServerWorld world = pointer.world();

        var facing = pointer.state().get(DispenserBlock.FACING);
        // if its UP or DOWN then use NORTH
        if (facing == Direction.UP) {
            facing = Direction.NORTH;
        }
        if (facing == Direction.DOWN) {
            facing = Direction.NORTH;
            origin = origin.down(4);
        }

        var leftCorner = origin.offset(facing.rotateYCounterclockwise());
        var rightCorner = origin.offset(facing.rotateYClockwise(), 2);
        world.setBlockState(leftCorner, Blocks.OBSIDIAN.getDefaultState());
        world.setBlockState(origin, Blocks.OBSIDIAN.getDefaultState());
        world.setBlockState(origin.offset(facing.rotateYClockwise()), Blocks.OBSIDIAN.getDefaultState());
        world.setBlockState(rightCorner, Blocks.OBSIDIAN.getDefaultState());
        world.setBlockState(leftCorner.offset(Direction.UP), Blocks.OBSIDIAN.getDefaultState());
        world.setBlockState(leftCorner.offset(Direction.UP, 2), Blocks.OBSIDIAN.getDefaultState());
        world.setBlockState(leftCorner.offset(Direction.UP, 3), Blocks.OBSIDIAN.getDefaultState());
        world.setBlockState(leftCorner.offset(Direction.UP, 4), Blocks.OBSIDIAN.getDefaultState());
        world.setBlockState(origin.offset(Direction.UP, 4), Blocks.OBSIDIAN.getDefaultState());
        world.setBlockState(origin.offset(facing.rotateYClockwise()).offset(Direction.UP, 4), Blocks.OBSIDIAN.getDefaultState());
        world.setBlockState(rightCorner.offset(Direction.UP), Blocks.OBSIDIAN.getDefaultState());
        world.setBlockState(rightCorner.offset(Direction.UP, 2), Blocks.OBSIDIAN.getDefaultState());
        world.setBlockState(rightCorner.offset(Direction.UP, 3), Blocks.OBSIDIAN.getDefaultState());
        world.setBlockState(rightCorner.offset(Direction.UP, 4), Blocks.OBSIDIAN.getDefaultState());

        world.setBlockState(origin.offset(Direction.UP, 1), Blocks.NETHER_PORTAL.getDefaultState().with(NetherPortalBlock.AXIS, facing.getAxis()).rotate(BlockRotation.CLOCKWISE_90));
        world.setBlockState(origin.offset(Direction.UP, 2), Blocks.NETHER_PORTAL.getDefaultState().with(NetherPortalBlock.AXIS, facing.getAxis()).rotate(BlockRotation.CLOCKWISE_90));
        world.setBlockState(origin.offset(Direction.UP, 3), Blocks.NETHER_PORTAL.getDefaultState().with(NetherPortalBlock.AXIS, facing.getAxis()).rotate(BlockRotation.CLOCKWISE_90));
        world.setBlockState(origin.offset(facing.rotateYClockwise()).offset(Direction.UP, 1), Blocks.NETHER_PORTAL.getDefaultState().with(NetherPortalBlock.AXIS, facing.getAxis()).rotate(BlockRotation.CLOCKWISE_90));
        world.setBlockState(origin.offset(facing.rotateYClockwise()).offset(Direction.UP, 2), Blocks.NETHER_PORTAL.getDefaultState().with(NetherPortalBlock.AXIS, facing.getAxis()).rotate(BlockRotation.CLOCKWISE_90));
        world.setBlockState(origin.offset(facing.rotateYClockwise()).offset(Direction.UP, 3), Blocks.NETHER_PORTAL.getDefaultState().with(NetherPortalBlock.AXIS, facing.getAxis()).rotate(BlockRotation.CLOCKWISE_90));

        return ItemStack.EMPTY;
    }
}
