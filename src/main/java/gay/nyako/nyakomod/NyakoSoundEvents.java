package gay.nyako.nyakomod;

import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NyakoSoundEvents {
    public static SoundEvent DISCORD = register("discord");
    public static SoundEvent WOLVES = register("wolves");
    public static SoundEvent COIN_COLLECT = register("coin_collect");
    public static SoundEvent SPUNCH_BLOCK = register("vine_boom");

    public static final BlockSoundGroup SPUNCH_BLOCK_SOUND_GROUP = new BlockSoundGroup(1.0f, 1.2f, SPUNCH_BLOCK, SoundEvents.BLOCK_STONE_STEP, SPUNCH_BLOCK, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);

    public static SoundEvent register(String name) {
        Identifier identifier = new Identifier("nyakomod", name);
        return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
    }
}
