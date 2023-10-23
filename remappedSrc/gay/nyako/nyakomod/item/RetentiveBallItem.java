package gay.nyako.nyakomod.item;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.mixin.client.keybinding.KeyBindingAccessor;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import I;
import java.util.List;

public class RetentiveBallItem extends Item {
    public RetentiveBallItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.LEFT) {
            return false;
        }

        if (!otherStack.isEmpty() && otherStack.isOf(Items.ENDER_EYE)) {
            var nbt = stack.getOrCreateNbt();
            var fuel = getFuel(stack);

            if (fuel > getMaxFuel() - getFuelPerEye()) {
                return false;
            }

            otherStack.setCount(otherStack.getCount() - 1);
            nbt.putInt("fuel", Math.min(fuel + getFuelPerEye(), getMaxFuel()));

            return true;
        }

        return false;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (useBall(itemStack, user, !user.isSneaking())) {
            return TypedActionResult.success(itemStack);
        }

        return TypedActionResult.fail(itemStack);
    }

    boolean hasLeftClicked = false;

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    public int calculateLevelExperienceCost(int level) {
        if (level >= 30) {
            return 112 + (level - 30) * 9;
        } else {
            return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
        }
    }

    public void syncXp(PlayerEntity player) {
        var level = player.experienceLevel;
        var newTotalXp = 0;
        for (var i = 0; i < level; i++) {
            newTotalXp += calculateLevelExperienceCost(level);
        }

        int nextLevelExperienceCost = player.getNextLevelExperience();
        float xpProgressPercent = player.experienceProgress;
        int xpToNextLevel = (int)(xpProgressPercent * nextLevelExperienceCost);
        int xpFromNextLevel = nextLevelExperienceCost - xpToNextLevel;

        if (xpToNextLevel != 0) {
            newTotalXp += xpFromNextLevel;
        }

        player.totalExperience = newTotalXp;
    }

    public boolean useBall(ItemStack stack, PlayerEntity player, boolean store) {
        var fuel = getFuel(stack);
        var storedXp = getXp(stack);

        int xpToStore = 0;

        int nextLevelExperienceCost = player.getNextLevelExperience();
        float xpProgressPercent = player.experienceProgress;
        int xpToNextLevel = (int)(xpProgressPercent * nextLevelExperienceCost);
        int xpFromNextLevel = nextLevelExperienceCost - xpToNextLevel;

        int level = player.experienceLevel;

        if (store) {
            if (level == 0 && xpToNextLevel == 0) {
                return false;
            }

            if (xpToNextLevel == 0) {
                xpToStore = -calculateLevelExperienceCost(level - 1);
            } else {
                xpToStore = -xpToNextLevel;
            }
        } else {
            xpToStore = Math.min(xpFromNextLevel, storedXp);
        }

        var clampedXp = Math.max(Math.min(xpToStore, fuel), -fuel);
        player.addExperience(clampedXp);

        syncXp(player);

        var nbt = stack.getOrCreateNbt();
        nbt.putInt("fuel", fuel - Math.abs(clampedXp));
        nbt.putInt("xp", storedXp + -clampedXp);

        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        var fuel = getFuel(stack);
        var xp = getXp(stack);
        var levels = calculateStoredLevels(stack);

        tooltip.add(Text.literal("XP is stored in the ball.").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Requires fuel. Right click").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("with ").formatted(Formatting.GRAY)
                .append(Text.literal("Ender Eyes").formatted(Formatting.DARK_AQUA))
                .append(Text.literal(" to refuel.").formatted(Formatting.GRAY)));
        tooltip.add(Text.literal(""));

        tooltip.add(Text.literal("Fuel: ").formatted(Formatting.GRAY).append(Text.literal(String.valueOf(fuel)).formatted(Formatting.GOLD)));
        tooltip.add(Text.literal("XP: ").formatted(Formatting.GRAY).append(Text.literal(String.valueOf(xp)).formatted(Formatting.GOLD)));
        tooltip.add(Text.literal("Effective Levels: ").formatted(Formatting.GRAY).append(Text.literal(String.valueOf(levels)).formatted(Formatting.GOLD)));
    }

    public int getXp(ItemStack stack) {
        var nbt = stack.getOrCreateNbt();
        if (nbt.contains("xp")) {
            return nbt.getInt("xp");
        }

        return 0;
    }

    public int calculateStoredLevels(ItemStack stack) {
        int xp = getXp(stack);
        int level = 0;

        while (true) {
            var neededXp = calculateLevelExperienceCost(level);

            if (neededXp > xp) {
                break;
            } else {
                level++;
                xp -= neededXp;
            }
        }

        return level;
    }

    public int getFuel(ItemStack stack) {
        var nbt = stack.getOrCreateNbt();
        if (nbt.contains("fuel")) {
            return nbt.getInt("fuel");
        }

        return 0;
    }

    public int getFuelPerEye() {
        return 500;
    }

    public int getMaxFuel() {
        return getFuelPerEye() * 10;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return MathHelper.packRgb(39, 234, 225);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return getFuel(stack) > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        var max = getMaxFuel();
        var stored = max - getFuel(stack);

        return Math.round(13.0f - (float)stored * 13.0f / (float)max);
    }
}
