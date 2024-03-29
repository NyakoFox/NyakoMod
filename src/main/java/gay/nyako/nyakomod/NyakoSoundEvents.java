package gay.nyako.nyakomod;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class NyakoSoundEvents {
    public static final SoundEvent SUPER_LUIGI          = register("super_luigi");
    public static final SoundEvent DISCORD              = register("discord");
    public static final SoundEvent WOLVES               = register("wolves");
    public static final SoundEvent COIN_COLLECT         = register("coin_collect");
    public static final SoundEvent SPUNCH_BLOCK         = register("vine_boom");
    public static final SoundEvent TRUE_BLOCK           = register("true");
    public static final SoundEvent NETHER_PORTAL_LAUNCH = register("nether_portal_launch");
    public static final SoundEvent SAMSUNG              = register("samsung");
    public static final SoundEvent UNLOCK               = register("unlock");
    public static final SoundEvent VENT                 = register("vent");
    public static final SoundEvent STICKER              = register("sticker");

    public static final SoundEvent MUSIC_DISC_WOLVES    = NyakoDiscs.WOLVES.soundEvent();
    public static final SoundEvent MUSIC_DISC_MASK      = NyakoDiscs.MASK.soundEvent();
    public static final SoundEvent MUSIC_DISC_CLUNK     = NyakoDiscs.CLUNK.soundEvent();
    public static final SoundEvent MUSIC_DISC_MERRY     = NyakoDiscs.MERRY.soundEvent();
    public static final SoundEvent MUSIC_DISC_WEEZED    = NyakoDiscs.WEEZED.soundEvent();
    public static final SoundEvent MUSIC_DISC_MOONLIGHT = NyakoDiscs.MOONLIGHT.soundEvent();
    public static final SoundEvent MUSIC_DISC_WELCOME   = NyakoDiscs.WELCOME.soundEvent();
    public static final SoundEvent MUSIC_DISC_SKIBIDI   = NyakoDiscs.SKIBIDI.soundEvent();
    public static final SoundEvent MUSIC_DISC_MERRY2    = NyakoDiscs.MERRY2.soundEvent();
    public static final SoundEvent MUSIC_DISC_SKIBIDI_REAL = NyakoDiscs.SKIBIDI_REAL.soundEvent();


    public static final BlockSoundGroup SPUNCH_BLOCK_SOUND_GROUP = new BlockSoundGroup(1.0f, 1.2f, SPUNCH_BLOCK, SoundEvents.BLOCK_STONE_STEP, SPUNCH_BLOCK, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);

    public static final SoundEvent ENTITY_DECAYED_AMBIENT = register("entity.decayed.ambient");
    public static final SoundEvent ENTITY_DECAYED_CONVERTED_TO_ZOMBIE = register("entity.decayed.converted_to_zombie");
    public static final SoundEvent ENTITY_DECAYED_DEATH = register("entity.decayed.death");
    public static final SoundEvent ENTITY_DECAYED_HURT = register("entity.decayed.hurt");
    public static final SoundEvent ENTITY_DECAYED_STEP = register("entity.decayed.step");
    public static final SoundEvent UI_FLETCHING_TABLE_TAKE_RESULT = register("ui.fletching_table.take_result");

    public static SoundEvent register(String name) {
        Identifier identifier = new Identifier("nyakomod", name);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static void register() {
        // include the class
    }
}
