package gay.nyako.nyakomod.item;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Optional;

public class RecallPotionItem extends Item {
    public RecallPotionItem(FabricItemSettings fabricItemSettings) {
        super(fabricItemSettings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof ServerPlayerEntity player) {
            Criteria.CONSUME_ITEM.trigger(player, stack);

            player.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }


            BlockPos blockPos = player.getSpawnPointPosition();
            float spawnAngle = player.getSpawnAngle();
            ServerWorld spawnWorld = world.getServer().getWorld(player.getSpawnPointDimension());

            if (blockPos == null)
            {
                blockPos = spawnWorld.getSpawnPos();
            }

            Optional<Vec3d> optional = PlayerEntity.findRespawnPosition(spawnWorld, blockPos, spawnAngle, false, true);

            if (blockPos != null && optional.isEmpty()) {
                blockPos = spawnWorld.getSpawnPos();
                optional = PlayerEntity.findRespawnPosition(spawnWorld, blockPos, spawnAngle, true, true);
            }

            if (optional.isPresent()) {
                float yaw;
                BlockState blockState = spawnWorld.getBlockState(blockPos);
                boolean bl3 = blockState.isOf(Blocks.RESPAWN_ANCHOR);
                Vec3d vec3d = optional.get();
                if (blockState.isIn(BlockTags.BEDS) || bl3) {
                    Vec3d vec3d2 = Vec3d.ofBottomCenter(blockPos).subtract(vec3d).normalize();
                    yaw = (float)MathHelper.wrapDegrees(MathHelper.atan2(vec3d2.z, vec3d2.x) * 57.2957763671875 - 90.0);
                } else {
                    yaw = spawnAngle;
                }
                player.refreshPositionAndAngles(vec3d.x, vec3d.y, vec3d.z, yaw, 0.0f);
                player.setSpawnPoint(spawnWorld.getRegistryKey(), blockPos, spawnAngle, true, false);
            }
            while (!spawnWorld.isSpaceEmpty(player) && player.getY() < (double)spawnWorld.getTopY()) {
                player.setPosition(player.getX(), player.getY() + 1.0, player.getZ());
            }

            FabricDimensions.teleport(player, spawnWorld, new TeleportTarget(player.getPos(), player.getVelocity(), player.getYaw(), player.getPitch()));


            player.getInventory().updateItems();

            if (!player.getAbilities().creativeMode) {
                if (stack.isEmpty()) {
                    return new ItemStack(Items.GLASS_BOTTLE);
                }
                player.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        user.emitGameEvent(GameEvent.DRINK);
        return stack;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

}
