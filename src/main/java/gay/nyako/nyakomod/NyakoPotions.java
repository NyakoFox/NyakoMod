package gay.nyako.nyakomod;

import gay.nyako.nyakomod.mixin.BrewingRecipeRegistryInvoker;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;

public class NyakoPotions {
    public static final Potion UNLUCK = Potions.register("unluck", new Potion(new StatusEffectInstance(StatusEffects.UNLUCK, 6000))); // 5 minutes
    public static final Potion HASTE = Potions.register("haste", new Potion(new StatusEffectInstance(StatusEffects.HASTE, 1200))); // 1 minute
    public static final Potion MINING_FATIGUE = Potions.register("mining_fatigue", new Potion(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 1200))); // 1 minute
    public static final Potion NAUSEA = Potions.register("nausea", new Potion(new StatusEffectInstance(StatusEffects.NAUSEA, 600))); // 30 seconds
    public static final Potion BLINDNESS = Potions.register("blindness", new Potion(new StatusEffectInstance(StatusEffects.BLINDNESS, 600))); // 30 seconds
    public static final Potion HUNGER = Potions.register("hunger", new Potion(new StatusEffectInstance(StatusEffects.HUNGER, 1200))); // 1 minute
    public static final Potion LONG_HUNGER = Potions.register("long_hunger", new Potion("hunger", new StatusEffectInstance(StatusEffects.HUNGER, 2400))); // 2 minutes
    public static final Potion HEALTH_BOOST = Potions.register("health_boost", new Potion(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 1200))); // 1 minute
    public static final Potion ABSORPTION = Potions.register("absorption", new Potion(new StatusEffectInstance(StatusEffects.ABSORPTION, 1200))); // 1 minute
    public static final Potion SATURATION = Potions.register("saturation", new Potion(new StatusEffectInstance(StatusEffects.SATURATION, 1200))); // 1 minute
    public static final Potion GLOWING = Potions.register("glowing", new Potion(new StatusEffectInstance(StatusEffects.GLOWING, 1200))); // 1 minute
    public static final Potion DOLPHINS_GRACE = Potions.register("dolphins_grace", new Potion(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 1200))); // 1 minute
    public static final Potion DARKNESS = Potions.register("darkness", new Potion(new StatusEffectInstance(StatusEffects.DARKNESS, 600))); // 30 seconds
    public static final Potion HUNTER = Potions.register("hunter", new Potion(new StatusEffectInstance(NyakoStatusEffects.HUNTER_STATUS_EFFECT, 3600))); // 3 minute
    public static final Potion LONG_HUNTER = Potions.register("hunter_long", new Potion("hunter", new StatusEffectInstance(NyakoStatusEffects.HUNTER_STATUS_EFFECT, 9600))); // 8 minutes
    public static final Potion SPILLED_MILK = Potions.register("spilled_milk", new Potion(new StatusEffectInstance(NyakoStatusEffects.SPILLED_MILK, 1200))); // 1 minute
    public static final Potion LONG_SPILLED_MILK = Potions.register("long_spilled_milk", new Potion("spilled_milk", new StatusEffectInstance(NyakoStatusEffects.SPILLED_MILK, 2400))); // 2 minutes
    public static final Potion LACTOSE_INTOLERANCE = Potions.register("lactose_intolerance", new Potion(new StatusEffectInstance(NyakoStatusEffects.LACTOSE_INTOLERANCE, 1200))); // 1 minute
    public static final Potion LONG_LACTOSE_INTOLERANCE = Potions.register("long_lactose_intolerance", new Potion("lactose_intolerance", new StatusEffectInstance(NyakoStatusEffects.LACTOSE_INTOLERANCE, 2400))); // 2 minutes

    public static void registerPotionsRecipes(){
        BrewingRecipeRegistryInvoker.invokeRegisterPotionRecipe(Potions.NIGHT_VISION, Items.ENDER_EYE, HUNTER);
        BrewingRecipeRegistryInvoker.invokeRegisterPotionRecipe(Potions.LONG_NIGHT_VISION, Items.ENDER_EYE, LONG_HUNTER);
        BrewingRecipeRegistryInvoker.invokeRegisterPotionRecipe(HUNTER, Items.REDSTONE, LONG_HUNTER);

        // Potion of hunger
        BrewingRecipeRegistryInvoker.invokeRegisterPotionRecipe(Potions.AWKWARD, Items.ROTTEN_FLESH, HUNGER);
        BrewingRecipeRegistryInvoker.invokeRegisterPotionRecipe(HUNGER, Items.REDSTONE, LONG_HUNGER);

        // Spilled milk
        BrewingRecipeRegistryInvoker.invokeRegisterPotionRecipe(HUNGER, Items.MILK_BUCKET, SPILLED_MILK);
        BrewingRecipeRegistryInvoker.invokeRegisterPotionRecipe(LONG_HUNGER, Items.MILK_BUCKET, LONG_SPILLED_MILK);
        BrewingRecipeRegistryInvoker.invokeRegisterPotionRecipe(SPILLED_MILK, Items.REDSTONE, LONG_SPILLED_MILK);

        // Lactose intolerance
        BrewingRecipeRegistryInvoker.invokeRegisterPotionRecipe(SPILLED_MILK, Items.FERMENTED_SPIDER_EYE, LACTOSE_INTOLERANCE);
        BrewingRecipeRegistryInvoker.invokeRegisterPotionRecipe(LONG_SPILLED_MILK, Items.FERMENTED_SPIDER_EYE, LONG_LACTOSE_INTOLERANCE);
        BrewingRecipeRegistryInvoker.invokeRegisterPotionRecipe(LACTOSE_INTOLERANCE, Items.REDSTONE, LONG_LACTOSE_INTOLERANCE);
    }
}
