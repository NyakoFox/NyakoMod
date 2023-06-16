package gay.nyako.nyakomod.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;

public class SmithingHammerItem extends Item {
    public SmithingHammerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var stack = context.getStack();
        var world = context.getWorld();
        var playerEntity = context.getPlayer();
        if (playerEntity == null) return ActionResult.PASS;
        world.playSound(playerEntity, playerEntity.getBlockPos(), SoundEvents.BLOCK_SMITHING_TABLE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);
        var broken = stack.damage(1, world.random, null);
        if (broken) {
            playerEntity.playEquipmentBreakEffects(stack);
            stack.decrement(1);
            return ActionResult.SUCCESS;
        }
        return ActionResult.SUCCESS;
    }
}
