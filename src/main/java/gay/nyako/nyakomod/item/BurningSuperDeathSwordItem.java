package gay.nyako.nyakomod.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;

public class BurningSuperDeathSwordItem extends SwordItem {
    public BurningSuperDeathSwordItem(FabricItemSettings settings) {
        super(ToolMaterials.NETHERITE, 3, -2.4f, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setFireTicks(30);
        return super.postHit(stack, target, attacker);
    }
}
