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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NyakoDiscs {
    public static final List<DiscGroup> MUSIC_DISCS = new ArrayList<>();

    public static final DiscGroup WOLVES = register("wolves", toSeconds(5, 1));
    public static final DiscGroup MASK = register("mask", toSeconds(2, 54));
    public static final DiscGroup CLUNK = register("clunk", toSeconds(2, 36));
    public static final DiscGroup MERRY = register("merry", toSeconds(10, 10));
    public static final DiscGroup WEEZED = register("weezed", toSeconds(4, 19));
    public static final DiscGroup MOONLIGHT = register("moonlight", toSeconds(3, 10));
    public static final DiscGroup WELCOME = register("welcome", toSeconds(3, 8));

    public static DiscGroup register(String name, int length) {
        var soundId = new Identifier("nyakomod", "music_disc." + name);
        var itemId = new Identifier("nyakomod", "music_disc_" + name);
        var soundEvent = new SoundEvent(soundId);
        var discItem = new CustomDiscItem(15, soundEvent, new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).rarity(Rarity.RARE), length);

        Registry.register(Registry.ITEM, itemId, discItem);
        Registry.register(Registry.SOUND_EVENT, soundId, soundEvent);

        var discGroup = new DiscGroup(soundId, itemId, soundEvent, discItem);
        MUSIC_DISCS.add(discGroup);
        return discGroup;
    }

    public record DiscGroup(
            Identifier soundId,
            Identifier itemId,
            SoundEvent soundEvent,
            Item item
    ) {}

    private static int toSeconds(int minutes, int seconds) {
        return minutes * 60 + seconds;
    }

    public static void registerModels(ItemModelGenerator itemModelGenerator) {
        for (var disc : MUSIC_DISCS) {
            itemModelGenerator.register(disc.item, Models.GENERATED);
        }
    }

}
