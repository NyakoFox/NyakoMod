package gay.nyako.nyakomod.block;

import gay.nyako.nyakomod.utils.CunkCoinUtils;
import I;
import gay.nyako.nyakomod.NyakoGacha;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MatterVortexBlock extends Block {

    public static long USAGE_PRICE = 500L; // 5 gold coins

    public MatterVortexBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        var count = CunkCoinUtils.countInventoryCoins(player.getInventory()) + CunkCoinUtils.countInventoryCoins(player.getEnderChestInventory());
        if (count < USAGE_PRICE) return ActionResult.SUCCESS;

        CunkCoinUtils.removeCoins(player, USAGE_PRICE);

        // Compute the total weight of all items together.
        double totalWeight = 0.0;
        for (NyakoGacha.GachaEntry i : NyakoGacha.GACHA_ENTRIES) {
            totalWeight += i.weight();
        }

        // Now choose a random item.
        int idx = 0;
        for (double r = Math.random() * totalWeight; idx < NyakoGacha.GACHA_ENTRIES.size() - 1; ++idx) {
            r -= NyakoGacha.GACHA_ENTRIES.get(idx).weight();
            if (r <= 0.0) break;
        }
        NyakoGacha.GachaEntry gachaEntry = NyakoGacha.GACHA_ENTRIES.get(idx);

        StringBuilder starText = new StringBuilder();
        // Let's use Math.max in case we accidentally use something... over 5.
        for (int i = 0; i < Math.max(5, gachaEntry.rarity()); i++) {
            if (i < (gachaEntry.rarity())) {
                starText.append("★");
            } else {
                starText.append("☆");
            }
        }
        MutableText starMutableText = Texts.setStyleIfAbsent((MutableText) Text.of(starText.toString()), Style.EMPTY.withColor(Formatting.YELLOW));

        MutableText actionBarText = (MutableText) Text.of("You have been given ");
        actionBarText.append(gachaEntry.name());
        actionBarText.append("! ");
        actionBarText.append(starMutableText);
        player.sendMessage(actionBarText, true);

        var stack = gachaEntry.itemStack().copy();
        EnchantmentHelper.enchant(world.random, stack, world.random.nextBetween(10,25), true);
        if (!player.giveItemStack(stack)) {
            player.dropItem(stack, true);
        };

        player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f);
        player.playSound(SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 1f, 1f);

        return ActionResult.SUCCESS;
    }
}