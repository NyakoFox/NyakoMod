package gay.nyako.nyakomod.item;

import D;
import Z;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MagnetItem extends Item {
    public MagnetItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!isEnabled(stack)) return;

        if (world.getTime() % 20 == 0) {
            var broken = stack.damage(1, world.random, null);
            if (broken) {
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.playEquipmentBreakEffects(stack);
                }
                stack.decrement(1);
                return;
            }
        }

        var pos = entity.getPos();
        var pos1 = new BlockPos(pos.x - 8, pos.y - 8, pos.z - 8);
        var pos2 = new BlockPos(pos.x + 8, pos.y + 8, pos.z + 8);
        var vecPos = new Vec3d(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5);
        var entities = world.getOtherEntities(entity, new Box(pos1, pos2), e -> e instanceof ItemEntity || e instanceof ExperienceOrbEntity);

        for (var currentEntity : entities) {
            var itemPos = currentEntity.getPos();
            var vecItemPos = new Vec3d(itemPos.x + 0.5, itemPos.y + 0.5, itemPos.z + 0.5);
            var vec = vecPos.subtract(vecItemPos);
            var distance = vec.length();
            var vecNormalized = vec.normalize();
            var vecNormalizedScaled = vecNormalized.multiply(0.04 * Math.max(8 - distance, 0));
            currentEntity.setVelocity(vecNormalizedScaled);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var stack = user.getStackInHand(hand);
        var nbt = stack.getOrCreateNbt();

        if (nbt.contains("enabled")) {
            nbt.putBoolean("enabled", !nbt.getBoolean("enabled"));
        } else {
            nbt.putBoolean("enabled", true);
        }

        if (isEnabled(stack)) {
            user.playSound(SoundEvents.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
        } else {
            user.playSound(SoundEvents.BLOCK_BEACON_DEACTIVATE, 1.0f, 1.0f);
        }

        return TypedActionResult.success(stack);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return isEnabled(stack);
    }

    public boolean isEnabled(ItemStack stack) {
        if (stack.hasNbt()) {
            var nbt = stack.getNbt();
            if (nbt != null && nbt.contains("enabled")) {
                return nbt.getBoolean("enabled");
            }
        }
        return false;
    }
}
