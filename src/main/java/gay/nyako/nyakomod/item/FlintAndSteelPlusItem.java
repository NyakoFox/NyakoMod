package gay.nyako.nyakomod.item;

import com.mojang.datafixers.util.Pair;
import gay.nyako.nyakomod.access.TntEntityAccess;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;

public class FlintAndSteelPlusItem extends Item {
    public FlintAndSteelPlusItem(FabricItemSettings fabricItemSettings) {
        super(fabricItemSettings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos;
        PlayerEntity playerEntity = context.getPlayer();
        World world = context.getWorld();

        if (playerEntity instanceof ServerPlayerEntity) {
            ItemStack itemStack = context.getStack();
            itemStack.damage(1, playerEntity, p -> p.sendToolBreakStatus(context.getHand()));
        }

        BlockState blockState = world.getBlockState(blockPos = context.getBlockPos());

        if (!world.isClient) {
            TntEntity tntEntity = new TntEntity(world, (double) blockPos.getX() + 0.5, blockPos.getY(), (double) blockPos.getZ() + 0.5, playerEntity);
            world.spawnEntity(tntEntity);
            world.playSound(null, tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.emitGameEvent(playerEntity, GameEvent.PRIME_FUSE, blockPos);
            ((TntEntityAccess) tntEntity).setCopyBlockState(blockState);

            ServerWorld serverWorld = (ServerWorld) world;

            BlockEntity blockEntity = blockState.hasBlockEntity() ? serverWorld.getBlockEntity(blockPos) : null;

            LootContext.Builder builder = new LootContext.Builder(serverWorld).random(serverWorld.random).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(blockPos)).parameter(LootContextParameters.TOOL, ItemStack.EMPTY).optionalParameter(LootContextParameters.BLOCK_ENTITY, blockEntity).optionalParameter(LootContextParameters.THIS_ENTITY, playerEntity);

            ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList = new ObjectArrayList<>();

            blockState.onStacksDropped(serverWorld, blockPos, ItemStack.EMPTY, false);
            blockState.getDroppedStacks(builder).forEach(stack -> tryMergeStack(objectArrayList, stack, blockPos));

            for (Pair<ItemStack, BlockPos> pair : objectArrayList) {
                Block.dropStack(serverWorld, pair.getSecond(), pair.getFirst());
            }

        }

        world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);


        return ActionResult.success(world.isClient());
    }


    private static void tryMergeStack(ObjectArrayList<Pair<ItemStack, BlockPos>> stacks, ItemStack stack, BlockPos pos) {
        int i = stacks.size();
        for (int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = stacks.get(j);
            ItemStack itemStack = pair.getFirst();
            if (!ItemEntity.canMerge(itemStack, stack)) continue;
            ItemStack itemStack2 = ItemEntity.merge(itemStack, stack, 16);
            stacks.set(j, Pair.of(itemStack2, pair.getSecond()));
            if (!stack.isEmpty()) continue;
            return;
        }
        stacks.add(Pair.of(stack, pos));
    }
}
