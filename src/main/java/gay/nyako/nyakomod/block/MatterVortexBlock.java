package gay.nyako.nyakomod.block;

import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
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

    public static long USAGE_PRICE = 2000L; // 20 gold coins

    public MatterVortexBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        var count = NyakoMod.countInventoryCoins(player.getInventory()) + NyakoMod.countInventoryCoins(player.getEnderChestInventory());
        if (count < USAGE_PRICE) return ActionResult.SUCCESS;

        NyakoMod.removeCoins(player, USAGE_PRICE);

        // Compute the total weight of all items together.
        double totalWeight = 0.0;
        for (NyakoMod.GachaEntry i : NyakoMod.gachaEntryList) {
            totalWeight += i.weight();
        }

        // Now choose a random item.
        int idx = 0;
        for (double r = Math.random() * totalWeight; idx < NyakoMod.gachaEntryList.size() - 1; ++idx) {
            r -= NyakoMod.gachaEntryList.get(idx).weight();
            if (r <= 0.0) break;
        }
        NyakoMod.GachaEntry gachaEntry = NyakoMod.gachaEntryList.get(idx);

        String starText = "";
        // Let's use Math.max in case we accidentally use something... over 5.
        for (int i = 0; i < Math.max(5, gachaEntry.rarity()); i++) {
            if (i < (gachaEntry.rarity())) {
                starText += "★";
            } else {
                starText += "☆";
            }
        }
        MutableText starMutableText = Texts.setStyleIfAbsent((MutableText) Text.of(starText), Style.EMPTY.withColor(Formatting.YELLOW));


        MutableText actionBarText = (MutableText) Text.of("You have been given ");
        actionBarText.append(gachaEntry.name());
        actionBarText.append("! ");
        actionBarText.append(starMutableText);
        player.sendMessage(actionBarText, true);

        if (!player.giveItemStack(gachaEntry.itemStack().copy())) {
            player.dropItem(gachaEntry.itemStack().copy(), true);
        };

        player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f);
        player.playSound(SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 1f, 1f);

        return ActionResult.SUCCESS;
    }
}