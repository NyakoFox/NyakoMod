package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.access.EntityAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.GiantEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class GrownFoamZombieItem extends Item {
    public GrownFoamZombieItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient) {
            return ActionResult.SUCCESS;
        }

        // Spawn Giant
        GiantEntity giant = new GiantEntity(EntityType.GIANT, context.getWorld());
        giant.setPosition(context.getHitPos().x, context.getHitPos().y, context.getHitPos().z);
        ((EntityAccess)giant).setFromSpawner(true);
        context.getWorld().spawnEntity(giant);

        ItemStack stack = context.getStack();
        stack.decrement(1);
        context.getPlayer().setStackInHand(context.getHand(), stack);

        return ActionResult.SUCCESS;
    }
}
