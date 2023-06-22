package gay.nyako.nyakomod;

import gay.nyako.nyakomod.block.SoundBlock;
import gay.nyako.nyakomod.effect.HunterStatusEffect;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NyakoStatusEffects {
    public static final StatusEffect HUNTER_STATUS_EFFECT = register("hunter", new HunterStatusEffect());

    public static StatusEffect register(String id, StatusEffect effect) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier("nyakomod", id), effect);
    }
}
