package gay.nyako.nyakomod.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleEasings;
import virtuoel.pehkui.api.ScaleTypes;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin extends Item {

    public BoneMealItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        ScaleData width = ScaleTypes.WIDTH.getScaleData(entity);
        width.setScaleTickDelay(0);
        width.setScale(width.getScale() * 1.2f);

        ScaleData height = ScaleTypes.HEIGHT.getScaleData(entity);
        height.setScaleTickDelay(0);
        height.setScale(height.getScale() * 1.2f);

        ScaleData dropped = ScaleTypes.DROPS.getScaleData(entity);
        dropped.setScaleTickDelay(0);
        dropped.setScale(dropped.getScale() * 1.2f);

        stack.decrement(1);
        return ActionResult.SUCCESS;
    }
}
