package gay.nyako.nyakomod.item.gacha;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

import java.util.*;

public class GachaItem extends Item {
    protected int rarity = 1;
    protected List<MutableText> tooltip = new ArrayList<>();

    public GachaItem(Settings settings) {
        super(settings);
    }

    public GachaItem(Settings settings, int rarity, MutableText lore) {
        this(settings, rarity);
        tooltip.add(lore);
    }

    public GachaItem(Settings settings, int rarity, List<MutableText> lore) {
        this(settings, rarity);
        for (MutableText text : lore) {
            tooltip.add(text);
        }
    }

    public GachaItem(Settings settings, int rarity) {
        super(settings);
        this.rarity = rarity;
    }

    public int getRarity() {
        return rarity;
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        switch (rarity) {
            case 1:
            case 2:
                return Rarity.COMMON;
            case 3:
                return Rarity.UNCOMMON;
            case 4:
                return Rarity.RARE;
            case 5:
                return Rarity.EPIC;
        }
        // Fallbacks...
        if (rarity > 5) return Rarity.EPIC;
        return Rarity.COMMON;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        for (MutableText text : this.tooltip) {
            tooltip.add(text);
        }

        String starText = "";
        // Let's use Math.max in case we accidentally use something... over 5.
        for (int i = 0; i < Math.max(5, rarity); i++) {
            if (i < rarity) {
                starText += "★";
            } else {
                starText += "☆";
            }
        }
        tooltip.add(Texts.setStyleIfAbsent((MutableText) Text.of(starText), Style.EMPTY.withColor(Formatting.YELLOW)));
        tooltip.add(Texts.setStyleIfAbsent((MutableText) Text.of("Gacha item."), Style.EMPTY.withColor(Formatting.LIGHT_PURPLE).withItalic(true)));
    }
}
