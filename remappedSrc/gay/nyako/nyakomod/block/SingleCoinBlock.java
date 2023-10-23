package gay.nyako.nyakomod.block;

import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.NyakoSoundEvents;
import gay.nyako.nyakomod.item.CoinItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class SingleCoinBlock extends Block implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final int MAX_COINS = 64;
    public static final IntProperty COINS = NyakoMod.COINS_PROPERTY;
    protected static final VoxelShape ONE_COIN_SHAPE               = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 1.0,  12.0);
    protected static final VoxelShape TWO_COINS_SHAPE              = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 1.0,  15.0);
    protected static final VoxelShape THREE_COINS_SHAPE            = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 1.0,  15.0);
    protected static final VoxelShape FOUR_COINS_SHAPE             = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0,  16.0);
    protected static final VoxelShape SECOND_LAYER_COINS_SHAPE     = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0,  16.0);
    protected static final VoxelShape THIRD_LAYER_COINS_SHAPE      = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 3.0,  16.0);
    protected static final VoxelShape FOURTH_LAYER_COINS_SHAPE     = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0,  16.0);
    protected static final VoxelShape FIFTH_LAYER_COINS_SHAPE      = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 5.0,  16.0);
    protected static final VoxelShape SIXTH_LAYER_COINS_SHAPE      = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0,  16.0);
    protected static final VoxelShape SEVENTH_LAYER_COINS_SHAPE    = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 7.0,  16.0);
    protected static final VoxelShape EIGHTH_LAYER_COINS_SHAPE     = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0,  16.0);
    protected static final VoxelShape NINTH_LAYER_COINS_SHAPE      = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 9.0,  16.0);
    protected static final VoxelShape TENTH_LAYER_COINS_SHAPE      = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 10.0, 16.0);
    protected static final VoxelShape ELEVENTH_LAYER_COINS_SHAPE   = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 11.0, 16.0);
    protected static final VoxelShape TWELFTH_LAYER_COINS_SHAPE    = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);
    protected static final VoxelShape THIRTEENTH_LAYER_COINS_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 13.0, 16.0);
    protected static final VoxelShape FOURTEENTH_LAYER_COINS_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);
    protected static final VoxelShape FIFTEENTH_LAYER_COINS_SHAPE  = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);
    protected static final VoxelShape SIXTEENTH_LAYER_COINS_SHAPE  = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    public SingleCoinBlock(Settings settings) {
        super(settings.nonOpaque());
        this.setDefaultState(
                this.stateManager.getDefaultState()
                        .with(COINS, 1)
                        .with(WATERLOGGED, false)
        );
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        return true;
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getStackInHand(hand).getItem() instanceof CoinItem) {
            return ActionResult.PASS;
        }

        world.playSound(pos.getX(), pos.getY(), pos.getZ(), NyakoSoundEvents.COIN_COLLECT, player.getSoundCategory(), 0.7f, 1f, true);
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
            return blockState
                    .with(COINS, Math.min(MAX_COINS, blockState.get(COINS) + 1))
                    .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
        }

        return this.getDefaultState()
                .with(COINS, 1)
                .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            // This is for 1.17 and below: world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
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
            default: return ONE_COIN_SHAPE;
            case 2: return TWO_COINS_SHAPE;
            case 3: return THREE_COINS_SHAPE;
            case 4: return FOUR_COINS_SHAPE;
            case 5:
            case 6:
            case 7:
            case 8: return SECOND_LAYER_COINS_SHAPE;
            case 9:
            case 10:
            case 11:
            case 12: return THIRD_LAYER_COINS_SHAPE;
            case 13:
            case 14:
            case 15:
            case 16: return FOURTH_LAYER_COINS_SHAPE;
            case 17:
            case 18:
            case 19:
            case 20: return FIFTH_LAYER_COINS_SHAPE;
            case 21:
            case 22:
            case 23:
            case 24: return SIXTH_LAYER_COINS_SHAPE;
            case 25:
            case 26:
            case 27:
            case 28: return SEVENTH_LAYER_COINS_SHAPE;
            case 29:
            case 30:
            case 31:
            case 32: return EIGHTH_LAYER_COINS_SHAPE;
            case 33:
            case 34:
            case 35:
            case 36: return NINTH_LAYER_COINS_SHAPE;
            case 37:
            case 38:
            case 39:
            case 40: return TENTH_LAYER_COINS_SHAPE;
            case 41:
            case 42:
            case 43:
            case 44: return ELEVENTH_LAYER_COINS_SHAPE;
            case 45:
            case 46:
            case 47:
            case 48: return TWELFTH_LAYER_COINS_SHAPE;
            case 49:
            case 50:
            case 51:
            case 52: return THIRTEENTH_LAYER_COINS_SHAPE;
            case 53:
            case 54:
            case 55:
            case 56: return FOURTEENTH_LAYER_COINS_SHAPE;
            case 57:
            case 58:
            case 59:
            case 60: return FIFTEENTH_LAYER_COINS_SHAPE;
            case 61:
            case 62:
            case 63:
            case 64: return SIXTEENTH_LAYER_COINS_SHAPE;
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(COINS, WATERLOGGED);
    }

}