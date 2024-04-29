package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.NyakoBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
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

        if (!isUsable(stack)) {
            return;
        }

        if (world.getTime() % 20 == 0) {
            var broken = stack.damage(1, world.random, null);
            if (broken) {
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.playEquipmentBreakEffects(stack);
                }

                var nbt = stack.getOrCreateNbt();
                nbt.putBoolean("enabled", false);
                return;
            }
        }

        var pos = entity.getPos();
        var pos1 = new BlockPos((int) (pos.x - 8), (int) (pos.y - 8), (int) (pos.z - 8));
        var pos2 = new BlockPos((int) (pos.x + 8), (int) (pos.y + 8), (int) (pos.z + 8));
        var vecPos = new Vec3d(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5);
        var entities = world.getOtherEntities(entity, new Box(pos1.toCenterPos(), pos2.toCenterPos()), e -> e instanceof ItemEntity || e instanceof ExperienceOrbEntity);

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
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.IRON_INGOT);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient) return ActionResult.PASS;
        if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() == NyakoBlocks.CHARGED_IRON_BLOCK)
        {
            var stack = context.getStack();
            stack.setDamage(Math.max(0, stack.getDamage() - 10));
            context.getWorld().playSound(null, context.getBlockPos(), SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.PLAYERS, 1.0f, 1.0f);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var stack = user.getStackInHand(hand);
        var nbt = stack.getOrCreateNbt();

        if (isUsable(stack)) {
            if (nbt.contains("enabled")) {
                nbt.putBoolean("enabled", !nbt.getBoolean("enabled"));
            } else {
                nbt.putBoolean("enabled", true);
            }
        }

        if (isEnabled(stack)) {
            user.playSound(SoundEvents.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
        } else {
            user.playSound(SoundEvents.BLOCK_BEACON_DEACTIVATE, 1.0f, 1.0f);
        }

        return TypedActionResult.success(stack);
    }

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage();
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return isEnabled(stack);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {

        return stack.isDamaged() && isUsable(stack);
    }

    public boolean isEnabled(ItemStack stack) {
        if (!isUsable(stack)) return false;

        if (stack.hasNbt()) {
            var nbt = stack.getNbt();
            if (nbt != null && nbt.contains("enabled")) {
                return nbt.getBoolean("enabled");
            }
        }
        return false;
    }
}
