package gay.nyako.nyakomod;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;

public class NyakoModPotion {
    public static final Potion UNLUCK = Potions.register("unluck", new Potion("unluck", new StatusEffectInstance(StatusEffects.UNLUCK, 6000))); // 5 minutes
    public static final Potion HASTE = Potions.register("haste", new Potion("haste", new StatusEffectInstance(StatusEffects.HASTE, 1200))); // 1 minute
    public static final Potion MINING_FATIGUE = Potions.register("mining_fatigue", new Potion("mining_fatigue", new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 1200))); // 1 minute
    public static final Potion NAUSEA = Potions.register("nausea", new Potion("nausea", new StatusEffectInstance(StatusEffects.NAUSEA, 600))); // 30 seconds
    public static final Potion BLINDNESS = Potions.register("blindness", new Potion("blindness", new StatusEffectInstance(StatusEffects.BLINDNESS, 600))); // 30 seconds
    public static final Potion HUNGER = Potions.register("hunger", new Potion("hunger", new StatusEffectInstance(StatusEffects.HUNGER, 1200))); // 1 minute
    public static final Potion HEALTH_BOOST = Potions.register("health_boost", new Potion("health_boost", new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 1200))); // 1 minute
    public static final Potion ABSORPTION = Potions.register("absorption", new Potion("absorption", new StatusEffectInstance(StatusEffects.ABSORPTION, 1200))); // 1 minute
    public static final Potion SATURATION = Potions.register("saturation", new Potion("saturation", new StatusEffectInstance(StatusEffects.SATURATION, 1200))); // 1 minute
    public static final Potion GLOWING = Potions.register("glowing", new Potion("glowing", new StatusEffectInstance(StatusEffects.GLOWING, 1200))); // 1 minute
    public static final Potion DOLPHINS_GRACE = Potions.register("dolphins_grace", new Potion("dolphins_grace", new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 1200))); // 1 minute
    public static final Potion DARKNESS = Potions.register("darkness", new Potion("darkness", new StatusEffectInstance(StatusEffects.DARKNESS, 600))); // 30 seconds
}
