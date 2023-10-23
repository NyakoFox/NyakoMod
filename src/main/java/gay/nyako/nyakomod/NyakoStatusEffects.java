package gay.nyako.nyakomod;

import gay.nyako.nyakomod.effect.HunterStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class NyakoStatusEffects {
    public static final StatusEffect HUNTER_STATUS_EFFECT = register("hunter", new HunterStatusEffect());

    public static StatusEffect register(String id, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier("nyakomod", id), effect);
    }
}
