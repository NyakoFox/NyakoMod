package gay.nyako.nyakomod.effect;

import gay.nyako.nyakomod.access.PlayerEntityAccess;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class SpilledMilkEffect extends StatusEffect {
    public SpilledMilkEffect() {
        super(StatusEffectCategory.HARMFUL, 0x989981);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 10 >> amplifier;
        return i > 0 ? duration % i == 0 : true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity playerEntity)
        {
            PlayerEntityAccess playerEntityAccess = (PlayerEntityAccess) playerEntity;
            if (playerEntityAccess.getMilk() > 0)
            {
                playerEntityAccess.setMilk(playerEntityAccess.getMilk() - 1);
            }
            if (playerEntityAccess.getMilkSaturation() > 0)
            {
                playerEntityAccess.setMilkSaturation(playerEntityAccess.getMilkSaturation() - 1);
            }
            if (playerEntityAccess.getMilkTimer() > 0)
            {
                playerEntityAccess.setMilkTimer(0);
            }
        }
    }
}
