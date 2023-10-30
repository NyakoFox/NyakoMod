package gay.nyako.nyakomod;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class NyakoItemTags {
    public static final TagKey<Item> ECHO_SPINES = TagKey.of(RegistryKeys.ITEM, new Identifier("nyakomod", "echo_spines"));
    public static final TagKey<Item> BENTHIC_SPINES = TagKey.of(RegistryKeys.ITEM, new Identifier("nyakomod", "benthic_spines"));
}
