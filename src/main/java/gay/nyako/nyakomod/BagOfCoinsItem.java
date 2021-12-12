package gay.nyako.nyakomod;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class BagOfCoinsItem extends Item {

    public BagOfCoinsItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (!otherStack.isEmpty()) {
            if (otherStack.isOf(NyakoMod.COPPER_COIN_ITEM) ||
                    otherStack.isOf(NyakoMod.GOLD_COIN_ITEM) ||
                    otherStack.isOf(NyakoMod.EMERALD_COIN_ITEM) ||
                    otherStack.isOf(NyakoMod.DIAMOND_COIN_ITEM) ||
                    otherStack.isOf(NyakoMod.NETHERITE_COIN_ITEM)) {

                player.playSound(NyakoMod.COIN_COLLECT_SOUND_EVENT, SoundCategory.MASTER, 0.7f, 1f);

                NbtCompound tag = stack.getOrCreateNbt();
                int copper = tag.getInt("copper");
                int gold = tag.getInt("gold");
                int emerald = tag.getInt("emerald");
                int diamond = tag.getInt("diamond");
                int netherite = tag.getInt("netherite");

                if (otherStack.isOf(NyakoMod.COPPER_COIN_ITEM)) {
                    copper += otherStack.getCount();
                }
                if (otherStack.isOf(NyakoMod.GOLD_COIN_ITEM)) {
                    gold += otherStack.getCount();
                }
                if (otherStack.isOf(NyakoMod.EMERALD_COIN_ITEM)) {
                    emerald += otherStack.getCount();
                }
                if (otherStack.isOf(NyakoMod.DIAMOND_COIN_ITEM)) {
                    diamond += otherStack.getCount();
                }
                if (otherStack.isOf(NyakoMod.NETHERITE_COIN_ITEM)) {
                    netherite += otherStack.getCount();
                }

                otherStack.setCount(0);

                tag.putInt("copper", copper);
                tag.putInt("gold", gold);
                tag.putInt("emerald", emerald);
                tag.putInt("diamond", diamond);
                tag.putInt("netherite", netherite);
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

        ItemStack copperStack = new ItemStack(NyakoMod.COPPER_COIN_ITEM);
        copperStack.setCount(copper);

        ItemStack goldStack = new ItemStack(NyakoMod.GOLD_COIN_ITEM);
        goldStack.setCount(gold);

        ItemStack emeraldStack = new ItemStack(NyakoMod.EMERALD_COIN_ITEM);
        emeraldStack.setCount(emerald);

        ItemStack diamondStack = new ItemStack(NyakoMod.DIAMOND_COIN_ITEM);
        diamondStack.setCount(diamond);

        ItemStack netheriteStack = new ItemStack(NyakoMod.NETHERITE_COIN_ITEM);
        netheriteStack.setCount(netherite);

        if (copper    > 0) user.getInventory().insertStack(copperStack);
        if (gold      > 0) user.getInventory().insertStack(goldStack);
        if (emerald   > 0) user.getInventory().insertStack(emeraldStack);
        if (diamond   > 0) user.getInventory().insertStack(diamondStack);
        if (netherite > 0) user.getInventory().insertStack(netheriteStack);

        user.playSound(NyakoMod.COIN_COLLECT_SOUND_EVENT, SoundCategory.MASTER, 0.7f, 1f);

        tag.putInt("copper", 0);
        tag.putInt("gold", 0);
        tag.putInt("emerald", 0);
        tag.putInt("diamond", 0);
        tag.putInt("netherite", 0);

        return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, user.getStackInHand(hand));
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
            tooltip.add(new LiteralText("§7§oThis bag is empty."));
        } else {
            tooltip.add(new LiteralText("Copper: §6" + copper));
            tooltip.add(new LiteralText("Gold: §e" + gold));
            tooltip.add(new LiteralText("Emerald: §2" + emerald));
            tooltip.add(new LiteralText("Diamond: §b" + diamond));
            tooltip.add(new LiteralText("Netherite: §8" + netherite));
        }
    }
}
