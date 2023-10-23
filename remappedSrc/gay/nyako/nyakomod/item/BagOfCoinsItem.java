package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.NyakoItems;
import gay.nyako.nyakomod.NyakoSoundEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class BagOfCoinsItem extends Item {

    public BagOfCoinsItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (!otherStack.isEmpty()) {
            if (otherStack.isOf(NyakoItems.COPPER_COIN) ||
                    otherStack.isOf(NyakoItems.GOLD_COIN) ||
                    otherStack.isOf(NyakoItems.EMERALD_COIN) ||
                    otherStack.isOf(NyakoItems.DIAMOND_COIN) ||
                    otherStack.isOf(NyakoItems.NETHERITE_COIN)) {

                player.playSound(NyakoSoundEvents.COIN_COLLECT, SoundCategory.MASTER, 0.7f, 1f);

                NbtCompound tag = stack.getOrCreateNbt();
                int copper = tag.getInt("copper");
                int gold = tag.getInt("gold");
                int emerald = tag.getInt("emerald");
                int diamond = tag.getInt("diamond");
                int netherite = tag.getInt("netherite");

                if (otherStack.isOf(NyakoItems.COPPER_COIN)) {
                    copper += otherStack.getCount();
                }
                if (otherStack.isOf(NyakoItems.GOLD_COIN)) {
                    gold += otherStack.getCount();
                }
                if (otherStack.isOf(NyakoItems.EMERALD_COIN)) {
                    emerald += otherStack.getCount();
                }
                if (otherStack.isOf(NyakoItems.DIAMOND_COIN)) {
                    diamond += otherStack.getCount();
                }
                if (otherStack.isOf(NyakoItems.NETHERITE_COIN)) {
                    netherite += otherStack.getCount();
                }

                otherStack.setCount(0);

                tag.putInt("copper", copper);
                tag.putInt("gold", gold);
                tag.putInt("emerald", emerald);
                tag.putInt("diamond", diamond);
                tag.putInt("netherite", netherite);

                rebalance(stack);
                return true;
            }
        }
        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        NbtCompound tag = user.getStackInHand(hand).getOrCreateNbt();
        int copper = tag.getInt("copper");
        int gold = tag.getInt("gold");
        int emerald = tag.getInt("emerald");
        int diamond = tag.getInt("diamond");
        int netherite = tag.getInt("netherite");

        if ((copper + gold + emerald + diamond + netherite) <= 0) {
            return super.use(world, user, hand);
        }

        tag.putBoolean("using", true);

        ItemStack copperStack = new ItemStack(NyakoItems.COPPER_COIN);
        copperStack.setCount(copper);

        ItemStack goldStack = new ItemStack(NyakoItems.GOLD_COIN);
        goldStack.setCount(gold);

        ItemStack emeraldStack = new ItemStack(NyakoItems.EMERALD_COIN);
        emeraldStack.setCount(emerald);

        ItemStack diamondStack = new ItemStack(NyakoItems.DIAMOND_COIN);
        diamondStack.setCount(diamond);

        ItemStack netheriteStack = new ItemStack(NyakoItems.NETHERITE_COIN);
        netheriteStack.setCount(netherite);

        if (copper    > 0) copper    = user.getInventory().addStack(copperStack);
        if (gold      > 0) gold      = user.getInventory().addStack(goldStack);
        if (emerald   > 0) emerald   = user.getInventory().addStack(emeraldStack);
        if (diamond   > 0) diamond   = user.getInventory().addStack(diamondStack);
        if (netherite > 0) netherite = user.getInventory().addStack(netheriteStack);

        user.playSound(NyakoSoundEvents.COIN_COLLECT, SoundCategory.MASTER, 0.7f, 1f);

        tag.putInt("copper", copper);
        tag.putInt("gold", gold);
        tag.putInt("emerald", emerald);
        tag.putInt("diamond", diamond);
        tag.putInt("netherite", netherite);
        tag.putBoolean("using", false);

        return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, user.getStackInHand(hand));
    }

    public void rebalance(ItemStack stack) {
        NbtCompound tag = stack.getOrCreateNbt();
        rebalanceCoin(tag, "copper", "gold");
        rebalanceCoin(tag, "gold", "emerald");
        rebalanceCoin(tag, "emerald", "diamond");
        rebalanceCoin(tag, "diamond", "netherite");
    }

    public void rebalanceCoin(NbtCompound tag, String key, String nextKey) {
        int value = tag.getInt(key);
        int nextValue = tag.getInt(nextKey);
        while (value >= 100) {
            value -= 100;
            nextValue += 1;
        }

        tag.putInt(key, value);
        tag.putInt(nextKey, nextValue);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        NbtCompound tag = itemStack.getOrCreateNbt();

        int copper = tag.getInt("copper");
        int gold = tag.getInt("gold");
        int emerald = tag.getInt("emerald");
        int diamond = tag.getInt("diamond");
        int netherite = tag.getInt("netherite");

        if ((copper + gold + emerald + diamond + netherite) <= 0) {
            tooltip.add(Text.literal("§7§oThis bag is empty."));
        } else {
            tooltip.add(Text.literal("Copper: §6" + copper));
            tooltip.add(Text.literal("Gold: §e" + gold));
            tooltip.add(Text.literal("Emerald: §2" + emerald));
            tooltip.add(Text.literal("Diamond: §b" + diamond));
            tooltip.add(Text.literal("Netherite: §8" + netherite));
        }
    }
}
