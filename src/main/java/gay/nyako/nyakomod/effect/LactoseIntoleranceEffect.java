package gay.nyako.nyakomod.effect;

import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.access.PlayerEntityAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.Nullable;

public class LactoseIntoleranceEffect extends StatusEffect {
    public LactoseIntoleranceEffect() {
        super(StatusEffectCategory.HARMFUL, 0x30693F);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 40 >> amplifier;
        return i > 0 ? duration % i == 0 : true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity playerEntity)
        {
            // Same as spilled milk... but hurt the player every time they loose milk
            PlayerEntityAccess playerEntityAccess = (PlayerEntityAccess) playerEntity;
            if (playerEntityAccess.getMilk() > 0)
            {
                playerEntityAccess.setMilk(playerEntityAccess.getMilk() - 2);
                playerEntity.damage(new DamageSource(
                        entity.getWorld().getRegistryManager()
                                .get(RegistryKeys.DAMAGE_TYPE)
                                .entryOf(NyakoMod.LACTOSE_INTOLERANCE_DAMAGE_TYPE)), 2);
            }
            if (playerEntityAccess.getMilkSaturation() > 0)
            {
                playerEntityAccess.setMilkSaturation(0);
            }
            if (playerEntityAccess.getMilkTimer() > 0)
            {
                playerEntityAccess.setMilkTimer(0);
            }
        }
    }
}
