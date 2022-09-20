package gay.nyako.nyakomod.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class DevNullItem extends Item {
    public DevNullItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.LEFT) {
            return false;
        }

        if (!otherStack.isEmpty()) {
            var nbt = stack.getOrCreateNbt();
            if (nbt.contains("stored_item")) {
                var stored = getStoredItem(stack);
                if (ItemStack.canCombine(otherStack, stored)) {
                    nbt.putInt("stored_count", nbt.getInt("stored_count") + otherStack.getCount());
                    otherStack.setCount(0);
                    return true;
                } else {
                    return false;
                }
            }

            var count = otherStack.getCount();
            otherStack.setCount(1);

            NbtCompound compound = new NbtCompound();
            otherStack.writeNbt(compound);

            nbt.put("stored_item", compound);
            nbt.putInt("stored_count", count);

            stack.setNbt(nbt);
            otherStack.setCount(0);

            return true;
        } else {
            var nbt = stack.getOrCreateNbt();
            if (!nbt.contains("stored_item")) {
                return false;
            }

            var stored = getStoredItem(stack);
            var count = nbt.getInt("stored_count");
            var extra = Math.min(64, count);

            var holdStack = stored.copy();
            holdStack.setCount(extra);
            nbt.putInt("stored_count", count - extra);

            dropContents(stack, player);
            cursorStackReference.set(holdStack);
            return true;
        }
    }

    public static ItemStack getStoredItem(ItemStack stack) {
        var nbt = stack.getOrCreateNbt();
        if (nbt.contains("stored_item")) {
            return ItemStack.fromNbt(nbt.getCompound("stored_item"));
        }

        return null;
    }

    public ItemUsageContext cloneWithNewStack(ItemUsageContext context, ItemStack stack) {
        return new ItemUsageContext(context.getWorld(), context.getPlayer(), context.getHand(), stack,
                new BlockHitResult(context.getHitPos(), context.getSide(), context.getBlockPos(), context.hitsInsideBlock()));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var stack = context.getStack();

        var stored = getStoredItem(stack);
        if (stored != null) {
            var nbt = stack.getOrCreateNbt();
            if (nbt.getInt("stored_count") <= 0) {
                return ActionResult.FAIL;
            }

            var player = context.getPlayer();
            var hand = context.getHand();
            player.setStackInHand(hand, stored);
            var originalCount = stored.getCount();

            var newContext = cloneWithNewStack(context, stored);

            var result = stored.getItem().useOnBlock(newContext);

            var diff = originalCount - stored.getCount();
            player.setStackInHand(hand, stack);
            nbt.putInt("stored_count", nbt.getInt("stored_count") - diff);

            return result;
        } else {
            return super.useOnBlock(context);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        return TypedActionResult.fail(itemStack);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, 0.8f, 0.8f + entity.getWorld().getRandom().nextFloat() * 0.4f);
    }

    private static boolean dropContents(ItemStack stack, PlayerEntity player) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        if (!nbtCompound.contains("stored_item")) {
            return false;
        }
        if (player instanceof ServerPlayerEntity) {
            var stored = getStoredItem(stack);
            var count = nbtCompound.getInt("stored_count");

            var extra = count % stored.getMaxCount();
            var extraStack = stored.copy();
            extraStack.setCount(extra);
            player.dropItem(extraStack, true);

            count -= extra;

            for (int i = 0; i < count; i += stored.getMaxCount()) {
                var dropStack = stored.copy();

                dropStack.setCount(stored.getMaxCount());

                player.dropItem(dropStack, true);
            }
        }
        nbtCompound.remove("stored_item");
        nbtCompound.remove("stored_count");
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound tag = stack.getOrCreateNbt();
        if (tag.contains("stored_item")) {
            var stored = tag.getCompound("stored_item");
            var s = ItemStack.fromNbt(stored);
            tooltip.add(Text.translatable("Stored: %s", s.getName()));
            tooltip.add(Text.translatable("Count: %s", tag.getInt("stored_count")));
        }
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return MathHelper.packRgb(255, 0, 0);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return stack.getOrCreateNbt().contains("stored_item");
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        var nbt = stack.getOrCreateNbt();
        if (nbt.contains("stored_item")) {
            var stored = getStoredItem(stack);
            var max = stored.getMaxCount();
            var storedCount = max - Math.min(max, nbt.getInt("stored_count"));

            return Math.round(13.0f - (float)storedCount * 13.0f / (float)max);
        }

        return 0;
    }
}
