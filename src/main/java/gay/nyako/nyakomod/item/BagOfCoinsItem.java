package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.NyakoModItem;
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

import gay.nyako.nyakomod.NyakoMod;

public class BagOfCoinsItem extends Item {

    public BagOfCoinsItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (!otherStack.isEmpty()) {
            if (otherStack.isOf(NyakoModItem.COPPER_COIN_ITEM) ||
                    otherStack.isOf(NyakoModItem.GOLD_COIN_ITEM) ||
                    otherStack.isOf(NyakoModItem.EMERALD_COIN_ITEM) ||
                    otherStack.isOf(NyakoModItem.DIAMOND_COIN_ITEM) ||
                    otherStack.isOf(NyakoModItem.NETHERITE_COIN_ITEM)) {

                player.playSound(NyakoMod.COIN_COLLECT_SOUND_EVENT, SoundCategory.MASTER, 0.7f, 1f);

                NbtCompound tag = stack.getOrCreateNbt();
                int copper = tag.getInt("copper");
                int gold = tag.getInt("gold");
                int emerald = tag.getInt("emerald");
                int diamond = tag.getInt("diamond");
                int netherite = tag.getInt("netherite");

                if (otherStack.isOf(NyakoModItem.COPPER_COIN_ITEM)) {
                    copper += otherStack.getCount();
                }
                if (otherStack.isOf(NyakoModItem.GOLD_COIN_ITEM)) {
                    gold += otherStack.getCount();
                }
                if (otherStack.isOf(NyakoModItem.EMERALD_COIN_ITEM)) {
                    emerald += otherStack.getCount();
                }
                if (otherStack.isOf(NyakoModItem.DIAMOND_COIN_ITEM)) {
                    diamond += otherStack.getCount();
                }
                if (otherStack.isOf(NyakoModItem.NETHERITE_COIN_ITEM)) {
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

        ItemStack copperStack = new ItemStack(NyakoModItem.COPPER_COIN_ITEM);
        copperStack.setCount(copper);

        ItemStack goldStack = new ItemStack(NyakoModItem.GOLD_COIN_ITEM);
        goldStack.setCount(gold);

        ItemStack emeraldStack = new ItemStack(NyakoModItem.EMERALD_COIN_ITEM);
        emeraldStack.setCount(emerald);

        ItemStack diamondStack = new ItemStack(NyakoModItem.DIAMOND_COIN_ITEM);
        diamondStack.setCount(diamond);

        ItemStack netheriteStack = new ItemStack(NyakoModItem.NETHERITE_COIN_ITEM);
        netheriteStack.setCount(netherite);

        if (copper    > 0) copper    = user.getInventory().addStack(copperStack);
        if (gold      > 0) gold      = user.getInventory().addStack(goldStack);
        if (emerald   > 0) emerald   = user.getInventory().addStack(emeraldStack);
        if (diamond   > 0) diamond   = user.getInventory().addStack(diamondStack);
        if (netherite > 0) netherite = user.getInventory().addStack(netheriteStack);

        user.playSound(NyakoMod.COIN_COLLECT_SOUND_EVENT, SoundCategory.MASTER, 0.7f, 1f);

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
