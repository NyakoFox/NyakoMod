package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class TotemOfDyingItem extends Item {
    public TotemOfDyingItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.damage(new DamageSource(
                user.getWorld().getRegistryManager()
                        .get(RegistryKeys.DAMAGE_TYPE)
                        .entryOf(NyakoMod.TOTEM_OF_DYING_DAMAGE_TYPE)), Float.MAX_VALUE);
        ItemStack stack = user.getStackInHand(hand);
        stack.decrement(1);
        user.setStackInHand(hand, stack);
        return TypedActionResult.success(stack);
    }
}
