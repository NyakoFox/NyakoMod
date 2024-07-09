package gay.nyako.nyakomod.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FishingCrateBlockItem extends BlockItem {
    public FishingCrateBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (world.isClient()) return TypedActionResult.pass(itemStack);
        user.playSound(SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.MASTER, 0.2f, ((user.getRandom().nextFloat() - user.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);

        itemStack.decrement(1);

        user.setStackInHand(hand, itemStack);

        return TypedActionResult.success(itemStack, true);
    }
}
