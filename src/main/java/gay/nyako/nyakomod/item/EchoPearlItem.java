package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.NyakoBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class EchoPearlItem extends Item {
    public EchoPearlItem(FabricItemSettings fabricItemSettings) {
        super(fabricItemSettings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var world = context.getWorld();
        var pos = context.getBlockPos();
        var state = world.getBlockState(pos);
        var block = state.getBlock();

        if (block.equals(Blocks.REINFORCED_DEEPSLATE))
        {
            var innerPos = pos.offset(context.getSide(), 1);
            // Check if the block is air
            if (world.getBlockState(innerPos).getBlock() == Blocks.AIR)
            {
                // Okay, let's check all directions and find the edges of the portal.
                int maxDistance = 64;
                BlockPos frameLeft = null;
                BlockPos frameRight = null;
                BlockPos frameTop = null;
                BlockPos frameBottom = null;
                for (int offset = 0; offset < maxDistance; offset++)
                {
                    // First, try to find the left edge. Let's check NORTH and WEST.
                    if (frameLeft == null)
                    {
                        var leftPos = innerPos.offset(Direction.NORTH, offset);
                        if (world.getBlockState(leftPos).getBlock() == Blocks.REINFORCED_DEEPSLATE)
                        {
                            frameLeft = leftPos;
                        }
                        else
                        {
                            leftPos = innerPos.offset(Direction.WEST, offset);
                            if (world.getBlockState(leftPos).getBlock() == Blocks.REINFORCED_DEEPSLATE)
                            {
                                frameLeft = leftPos;
                            }
                        }
                    }
                    // Next, try to find the right edge. Let's check SOUTH and EAST.
                    if (frameRight == null)
                    {
                        var rightPos = innerPos.offset(Direction.SOUTH, offset);
                        if (world.getBlockState(rightPos).getBlock() == Blocks.REINFORCED_DEEPSLATE)
                        {
                            frameRight = rightPos;
                        }
                        else
                        {
                            rightPos = innerPos.offset(Direction.EAST, offset);
                            if (world.getBlockState(rightPos).getBlock() == Blocks.REINFORCED_DEEPSLATE)
                            {
                                frameRight = rightPos;
                            }
                        }
                    }
                    // Next, try to find the top edge. Let's check UP.
                    if (frameTop == null)
                    {
                        var topPos = innerPos.offset(Direction.UP, offset);
                        if (world.getBlockState(topPos).getBlock() == Blocks.REINFORCED_DEEPSLATE)
                        {
                            frameTop = topPos;
                        }
                    }
                    // Finally, try to find the bottom edge. Let's check DOWN.
                    if (frameBottom == null)
                    {
                        var bottomPos = innerPos.offset(Direction.DOWN, offset);
                        if (world.getBlockState(bottomPos).getBlock() == Blocks.REINFORCED_DEEPSLATE)
                        {
                            frameBottom = bottomPos;
                        }
                    }
                }
                if (frameLeft == null) return ActionResult.PASS;
                if (frameRight == null) return ActionResult.PASS;
                if (frameTop == null) return ActionResult.PASS;
                if (frameBottom == null) return ActionResult.PASS;

                var topLeft = new BlockPos(frameLeft.getX(), frameTop.getY(), frameLeft.getZ());
                var topRight = new BlockPos(frameRight.getX(), frameTop.getY(), frameRight.getZ());
                var bottomLeft = new BlockPos(frameLeft.getX(), frameBottom.getY(), frameLeft.getZ());
                var bottomRight = new BlockPos(frameRight.getX(), frameBottom.getY(), frameRight.getZ());

                // Check if sides are reinforced deepslate
                for (int i = bottomLeft.getY(); i < topLeft.getY(); i++)
                {
                    var leftPos = new BlockPos(bottomLeft.getX(), i, bottomLeft.getZ());
                    if (world.getBlockState(leftPos).getBlock() != Blocks.REINFORCED_DEEPSLATE)
                    {
                        return ActionResult.PASS;
                    }
                    var rightPos = new BlockPos(bottomRight.getX(), i, bottomRight.getZ());
                    if (world.getBlockState(rightPos).getBlock() != Blocks.REINFORCED_DEEPSLATE)
                    {
                        return ActionResult.PASS;
                    }
                }
                // Check if top and bottom are reinforced deepslate
                // First we have to figure out whether the portal is on the X axis or the Z axis.
                // Let's just compare left and right values.
                boolean isXAxis = frameLeft.getX() != frameRight.getX();

                if (isXAxis)
                {
                    for (int i = bottomLeft.getX(); i < bottomRight.getX(); i++)
                    {
                        var bottomPos = new BlockPos(i, bottomLeft.getY(), bottomLeft.getZ());
                        if (world.getBlockState(bottomPos).getBlock() != Blocks.REINFORCED_DEEPSLATE)
                        {
                            return ActionResult.PASS;
                        }
                        var topPos = new BlockPos(i, topLeft.getY(), topLeft.getZ());
                        if (world.getBlockState(topPos).getBlock() != Blocks.REINFORCED_DEEPSLATE)
                        {
                            return ActionResult.PASS;
                        }
                    }
                } else {
                    for (int i = bottomLeft.getZ(); i < bottomRight.getZ(); i++)
                    {
                        var bottomPos = new BlockPos(bottomLeft.getX(), bottomLeft.getY(), i);
                        if (world.getBlockState(bottomPos).getBlock() != Blocks.REINFORCED_DEEPSLATE)
                        {
                            return ActionResult.PASS;
                        }
                        var topPos = new BlockPos(topLeft.getX(), topLeft.getY(), i);
                        if (world.getBlockState(topPos).getBlock() != Blocks.REINFORCED_DEEPSLATE)
                        {
                            return ActionResult.PASS;
                        }
                    }
                }

                // We have a valid portal, it's time to light!
                // Loop from top left to bottom right, and light the portal.
                int startX = Math.min(topLeft.getX(), bottomRight.getX());
                int endX = Math.max(topLeft.getX(), bottomRight.getX());
                int startY = Math.min(topLeft.getY(), bottomRight.getY());
                int endY = Math.max(topLeft.getY(), bottomRight.getY());
                int startZ = Math.min(topLeft.getZ(), bottomRight.getZ());
                int endZ = Math.max(topLeft.getZ(), bottomRight.getZ());
                for (int x = startX; x <= endX; x++)
                {
                    for (int y = startY; y <= endY; y++)
                    {
                        for (int z = startZ; z <= endZ; z++)
                        {
                            var newPos = new BlockPos(x, y, z);
                            if (world.getBlockState(newPos).getBlock() == Blocks.AIR || world.getBlockState(newPos).getBlock() == Blocks.SCULK_VEIN)
                            {
                                BlockState portalState = NyakoBlocks.ECHO_PORTAL.getDefaultState().with(NetherPortalBlock.AXIS, isXAxis ? Direction.Axis.X : Direction.Axis.Z);
                                world.setBlockState(newPos, portalState);
                            }
                        }
                    }
                }

                context.getStack().decrement(1);

                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }
}
