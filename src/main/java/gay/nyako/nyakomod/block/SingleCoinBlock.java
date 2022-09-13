package gay.nyako.nyakomod.block;

import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.item.CoinItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SingleCoinBlock extends Block {
    public static final int MAX_COINS = 3;
    public static final IntProperty COINS = NyakoMod.COINS_PROPERTY;
    protected static final VoxelShape ONE_COIN_SHAPE = Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 6.0, 10.0);
    protected static final VoxelShape TWO_COINS_SHAPE = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 6.0, 13.0);
    protected static final VoxelShape THREE_COINS_SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 6.0, 14.0);

    public SingleCoinBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(COINS, 1));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getStackInHand(hand).getItem() instanceof CoinItem) {
            return ActionResult.PASS;
        }

        world.playSound(pos.getX(), pos.getY(), pos.getZ(), NyakoMod.COIN_COLLECT_SOUND_EVENT, player.getSoundCategory(), 0.7f, 1f, true);
        var stack = new ItemStack(asItem());
        stack.setCount(1);
        player.getInventory().offerOrDrop(stack);

        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        int newCount = Math.max(0, state.get(COINS) - 1);
        if (newCount == 0) {
            world.removeBlock(pos, false);
        } else {
            world.setBlockState(pos, state.with(COINS, newCount), Block.NOTIFY_ALL);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (blockState.isOf(this)) {
            return blockState.with(COINS, Math.min(MAX_COINS, blockState.get(COINS) + 1));
        }
        return super.getPlacementState(ctx);
    }


    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        if (!context.shouldCancelInteraction() && context.getStack().isOf(this.asItem()) && state.get(COINS) < MAX_COINS) {
            return true;
        }
        return super.canReplace(state, context);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(COINS)) {
            default: {
                return ONE_COIN_SHAPE;
            }
            case 2: {
                return TWO_COINS_SHAPE;
            }
            case 3: {
                return THREE_COINS_SHAPE;
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(COINS);
    }

}