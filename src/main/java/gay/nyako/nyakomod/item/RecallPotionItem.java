package gay.nyako.nyakomod.item;

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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
        }
        if (!world.isClient) {
            if (playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                BlockPos spawnBlockPos = serverPlayerEntity.getSpawnPointPosition();
                float spawnAngle = serverPlayerEntity.getSpawnAngle();
                ServerWorld spawnWorld = world.getServer().getWorld(serverPlayerEntity.getSpawnPointDimension());
                Optional<Vec3d> respawnPos = PlayerEntity.findRespawnPosition(spawnWorld, spawnBlockPos, spawnAngle, true, true);
                if (respawnPos.isPresent()) {
                    float yaw;
                    BlockState blockState = spawnWorld.getBlockState(spawnBlockPos);
                    Vec3d vec3d = respawnPos.get();
                    if (blockState.isIn(BlockTags.BEDS) || blockState.isOf(Blocks.RESPAWN_ANCHOR)) {
                        Vec3d vec3d2 = Vec3d.ofBottomCenter(spawnBlockPos).subtract(vec3d).normalize();
                        yaw = (float) MathHelper.wrapDegrees(MathHelper.atan2(vec3d2.z, vec3d2.x) * 57.2957763671875 - 90.0);
                    } else {
                        yaw = spawnAngle;
                    }
                    serverPlayerEntity.refreshPositionAndAngles(vec3d.x, vec3d.y, vec3d.z, yaw, 0.0f);
                } else if (spawnBlockPos != null) {
                    serverPlayerEntity.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.NO_RESPAWN_BLOCK, GameStateChangeS2CPacket.DEMO_OPEN_SCREEN));
                }
                while (!spawnWorld.isSpaceEmpty(serverPlayerEntity) && serverPlayerEntity.getY() < (double)spawnWorld.getTopY()) {
                    serverPlayerEntity.setPosition(serverPlayerEntity.getX(), serverPlayerEntity.getY() + 1.0, serverPlayerEntity.getZ());
                }
                serverPlayerEntity.teleport(spawnWorld, spawnBlockPos.getX(), spawnBlockPos.getY(), spawnBlockPos.getZ(), serverPlayerEntity.getYaw(), serverPlayerEntity.getPitch());
            }
        }
        if (playerEntity != null) {
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!playerEntity.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }
        if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }
            if (playerEntity != null) {
                playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
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
