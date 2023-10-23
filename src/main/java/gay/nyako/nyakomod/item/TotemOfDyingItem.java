package gay.nyako.nyakomod.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class TotemOfDyingItem extends Item {
    public TotemOfDyingItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.damage(user.getDamageSources().genericKill(), Float.MAX_VALUE);
        ItemStack stack = user.getStackInHand(hand);
        stack.decrement(1);
        user.setStackInHand(hand, stack);
        return TypedActionResult.success(stack);
    }
}
