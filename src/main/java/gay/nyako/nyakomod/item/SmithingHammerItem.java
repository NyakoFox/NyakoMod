package gay.nyako.nyakomod.item;

import net.minecraft.block.*;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SmithingHammerItem extends Item {
    public SmithingHammerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var stack = context.getStack();
        var world = context.getWorld();
        var playerEntity = context.getPlayer();
        if (playerEntity == null) return ActionResult.PASS;
        hitBlock(world, context.getBlockPos(), playerEntity);
        world.playSound(playerEntity, playerEntity.getBlockPos(), SoundEvents.BLOCK_SMITHING_TABLE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);
        var broken = stack.damage(1, world.random, null);
        if (broken) {
            playerEntity.playEquipmentBreakEffects(stack);
            stack.decrement(1);
            return ActionResult.SUCCESS;
        }
        return ActionResult.SUCCESS;
    }

    public void hitBlock(World world, BlockPos pos, PlayerEntity player) {
        BlockState state = world.getBlockState(pos);
        BlockState newState = null;
        if (state.isOf(Blocks.STONE_BRICKS)) newState = Blocks.CRACKED_STONE_BRICKS.getDefaultState();
        if (state.isOf(Blocks.STONE)) newState = Blocks.COBBLESTONE.getDefaultState();
        if (state.isOf(Blocks.STONE_SLAB)) newState = Blocks.COBBLESTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, state.get(SlabBlock.TYPE));
        if (state.isOf(Blocks.STONE_STAIRS)) newState = Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, state.get(StairsBlock.FACING)).with(StairsBlock.HALF, state.get(StairsBlock.HALF));
        if (state.isOf(Blocks.STONE_BRICK_STAIRS)) newState = Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, state.get(StairsBlock.FACING)).with(StairsBlock.HALF, state.get(StairsBlock.HALF));
        if (state.isOf(Blocks.STONE_BRICK_SLAB)) newState = Blocks.COBBLESTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, state.get(SlabBlock.TYPE));
        if (state.isOf(Blocks.STONE_BRICK_WALL)) newState = Blocks.COBBLESTONE_WALL.getDefaultState();
        if (state.isOf(Blocks.MOSSY_STONE_BRICKS)) newState = Blocks.MOSSY_COBBLESTONE.getDefaultState();
        if (state.isOf(Blocks.MOSSY_STONE_BRICK_STAIRS)) newState = Blocks.MOSSY_COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, state.get(StairsBlock.FACING)).with(StairsBlock.HALF, state.get(StairsBlock.HALF));
        if (state.isOf(Blocks.MOSSY_STONE_BRICK_SLAB)) newState = Blocks.MOSSY_COBBLESTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, state.get(SlabBlock.TYPE));
        if (state.isOf(Blocks.MOSSY_STONE_BRICK_WALL)) newState = Blocks.MOSSY_COBBLESTONE_WALL.getDefaultState();
        if (state.isOf(Blocks.DEEPSLATE_BRICKS)) newState = Blocks.CRACKED_DEEPSLATE_BRICKS.getDefaultState();
        if (state.isOf(Blocks.DEEPSLATE_TILES)) newState = Blocks.CRACKED_DEEPSLATE_TILES.getDefaultState();
        if (state.isOf(Blocks.NETHER_BRICKS)) newState = Blocks.CRACKED_NETHER_BRICKS.getDefaultState();
        if (state.isOf(Blocks.POLISHED_BLACKSTONE_BRICKS)) newState = Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.getDefaultState();
        if (state.isOf(Blocks.GLASS)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.GLASS_PANE)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.BLACK_STAINED_GLASS)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.BLACK_STAINED_GLASS_PANE)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.BLUE_STAINED_GLASS)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.BLUE_STAINED_GLASS_PANE)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.BROWN_STAINED_GLASS)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.BROWN_STAINED_GLASS_PANE)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.CYAN_STAINED_GLASS)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.CYAN_STAINED_GLASS_PANE)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.GRAY_STAINED_GLASS)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.GRAY_STAINED_GLASS_PANE)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.GREEN_STAINED_GLASS)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.GREEN_STAINED_GLASS_PANE)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.LIGHT_BLUE_STAINED_GLASS)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.LIGHT_GRAY_STAINED_GLASS)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.LIME_STAINED_GLASS)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.LIME_STAINED_GLASS_PANE)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.MAGENTA_STAINED_GLASS)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.MAGENTA_STAINED_GLASS_PANE)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.ORANGE_STAINED_GLASS)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.ORANGE_STAINED_GLASS_PANE)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.PINK_STAINED_GLASS)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.PINK_STAINED_GLASS_PANE)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.PURPLE_STAINED_GLASS)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.PURPLE_STAINED_GLASS_PANE)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.RED_STAINED_GLASS)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.RED_STAINED_GLASS_PANE)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.WHITE_STAINED_GLASS)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.WHITE_STAINED_GLASS_PANE)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.YELLOW_STAINED_GLASS)) newState = Blocks.AIR.getDefaultState();
        if (state.isOf(Blocks.YELLOW_STAINED_GLASS_PANE)) newState = Blocks.AIR.getDefaultState();

        if (newState != null) {
            BlockSoundGroup blockSoundGroup = state.getSoundGroup();
            world.playSound(player, pos, blockSoundGroup.getBreakSound(), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0f) / 2.0f, blockSoundGroup.getPitch() * 0.8f);
            world.addBlockBreakParticles(pos, state);
            world.setBlockState(pos, newState, Block.NOTIFY_ALL | Block.SKIP_DROPS);
        }
    }
}
