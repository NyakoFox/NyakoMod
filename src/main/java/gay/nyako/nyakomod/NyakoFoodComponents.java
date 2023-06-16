package gay.nyako.nyakomod;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class NyakoFoodComponents {
    public static final FoodComponent DIAMOND_APPLE = new FoodComponent.Builder().hunger(6).saturationModifier(1.6f).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 1200, 1), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 4), 1.0f).alwaysEdible().build();
    public static final FoodComponent EMERALD_APPLE = new FoodComponent.Builder().hunger(4).saturationModifier(1.2f).statusEffect(new StatusEffectInstance(StatusEffects.LUCK, 2400, 2), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 600, 0), 1.0f).alwaysEdible().build();
}
