package gay.nyako.nyakomod;

import gay.nyako.nyakomod.item.CustomDiscItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class NyakoModDisc {
    public static final Map<String, NyakoModDisc> MUSIC_DISCS = new HashMap<>();

    public final Identifier soundId;
    public final Identifier itemId;
    public final SoundEvent soundEvent;
    public final Item discItem;

    public NyakoModDisc(String name, int length) {
        soundId = new Identifier("nyakomod", "music_disc." + name);
        itemId = new Identifier("nyakomod", "music_disc_" + name);
        soundEvent = new SoundEvent(soundId);
        discItem = new CustomDiscItem(15, soundEvent, new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).rarity(Rarity.RARE), length);
    }

    public void register() {
        Registry.register(Registry.ITEM, itemId, discItem);
        Registry.register(Registry.SOUND_EVENT, soundId, soundEvent);
    }

    public void registerModel(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(discItem, Models.GENERATED);
    }

    private static void createSong(String name, int length) {
        MUSIC_DISCS.put(name, new NyakoModDisc(name, length));
    }

    private static int toSeconds(int minutes, int seconds) {
        return minutes * 60 + seconds;
    }

    private static void createAllDiscs() {
        createSong("wolves", toSeconds(5, 1));
        createSong("mask", toSeconds(2, 54));
        createSong("clunk", toSeconds(2, 36));
        createSong("merry", toSeconds(10, 10));
        createSong("weezed", toSeconds(4, 19));
    }

    public static void registerAll() {
        for (var entry : MUSIC_DISCS.entrySet()) {
            entry.getValue().register();
        }
    }

    public static void registerModels(ItemModelGenerator itemModelGenerator) {
        for (var entry : MUSIC_DISCS.entrySet()) {
            entry.getValue().registerModel(itemModelGenerator);
        }
    }

    public static NyakoModDisc get(String name) {
        return MUSIC_DISCS.get(name);
    }

    static {
        createAllDiscs();
    }
}
