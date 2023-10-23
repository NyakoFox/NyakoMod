package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.access.PlayerEntityAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class TestItem extends Item {
    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var access = (PlayerEntityAccess) user;
        if (user.isSneaking()) {
            access.setMilk(access.getMilk() - 1);
        } else {
            access.setMilk(access.getMilk() + 1);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
