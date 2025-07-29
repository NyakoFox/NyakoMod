package gay.nyako.nyakomod;

import gay.nyako.nyakomod.effect.HunterStatusEffect;
import gay.nyako.nyakomod.effect.LactoseIntoleranceEffect;
import gay.nyako.nyakomod.effect.SpilledMilkEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class NyakoStatusEffects {
    public static final StatusEffect HUNTER_STATUS_EFFECT = register("hunter", new HunterStatusEffect());
    public static final StatusEffect LACTOSE_INTOLERANCE = register("lactose_intolerance", new LactoseIntoleranceEffect());
    public static final StatusEffect SPILLED_MILK = register("spilled_milk", new SpilledMilkEffect());

    public static StatusEffect register(String id, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, NyakoMod.id(id), effect);
    }
}
