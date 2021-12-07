package gay.nyako.nyakomod;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class StaffOfSmitingItem extends Item {

    public StaffOfSmitingItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        LightningEntity lightningBolt = new LightningEntity(EntityType.LIGHTNING_BOLT, target.world);
        lightningBolt.setPos(target.getX(), target.getY(), target.getZ());
        target.world.spawnEntity(lightningBolt);
        target.addVelocity(0d, 5d, 0d);
        return true;
    };
}
