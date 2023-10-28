package gay.nyako.nyakomod.block;

import gay.nyako.nyakomod.NyakoItems;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SproutHeartBlock extends TallFlowerBlock {
    public static final BooleanProperty HEART = BooleanProperty.of("heart");

    public SproutHeartBlock(FabricBlockSettings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(HEART, false).with(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        BlockPos blockPos = pos.up();
        world.setBlockState(blockPos, TallPlantBlock.withWaterloggedState(world, blockPos, state.with(HALF, DoubleBlockHalf.UPPER)), Block.NOTIFY_ALL);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            if (state.get(HEART)) {
                world.setBlockState(pos, state.with(HEART, false));
                BlockState state2 = world.getBlockState(pos.down());
                world.setBlockState(pos.down(), state2.with(HEART, false));

                ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, new ItemStack(NyakoItems.HEART_BERRY));
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);

                return ActionResult.SUCCESS;
            }
            if (player.getStackInHand(hand).getItem() == NyakoItems.HEART_BERRY) {
                world.setBlockState(pos, state.with(HEART, true));
                BlockState state2 = world.getBlockState(pos.down());
                world.setBlockState(pos.down(), state2.with(HEART, true));

                if (!player.isCreative()) {
                    player.getStackInHand(hand).decrement(1);
                }

                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HEART);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(HEART, false);
    }
}
