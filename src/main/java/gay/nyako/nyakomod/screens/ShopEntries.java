package gay.nyako.nyakomod.screens;

import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class ShopEntries {
    public static List<ShopData> shops = new ArrayList<>();

    public static final ShopData MAIN = register(new ShopData("main"));

    public static ShopData register(ShopData shopData) {
        shops.add(shopData);
        return shopData;
    }

    public static ShopData getShop(String id) {
        for (ShopData shop : shops) {
            if (shop.id.equals(id)) {
                return shop;
            }
        }

        return null;
    }

    public static void registerShops() {
        var diamonbItem = new ItemStack(Items.DIAMOND);
        diamonbItem.setCustomName(Text.literal("Diamonb #blessed").formatted(Formatting.AQUA));
        diamonbItem.addEnchantment(Enchantments.AQUA_AFFINITY, 1);
        diamonbItem.addHideFlag(ItemStack.TooltipSection.ENCHANTMENTS);
        MAIN.add(new ShopEntry(
                List.of(diamonbItem),
                48562,
                Text.of("Bwessed Diamonb"),
                Text.of("This diamond has been enriched by the seeds of The Ender, discovered long ago. Ever since, it has brought great luck and joy to those who wish upon it.\n" +
                        "\n" +
                        "Just kidding, it's a rock.")
        ));

        var livvieItem = new ItemStack(Items.PAINTING);
        livvieItem.setCustomName(Text.literal("Livvie").formatted(Formatting.LIGHT_PURPLE));
        MAIN.add(new ShopEntry(
                List.of(livvieItem),
                100000000,
                Text.of("Livvie"),
                Text.of("Picture of livvie  [so cute]  I want it so badly")
        ));

        var petSummoner = new ItemStack(NyakoMod.PET_SPRITE_SUMMON_ITEM);
        MAIN.add(new ShopEntry(
                List.of(petSummoner),
                1000000,
                Text.of("Pet Summoner"),
                Text.of("Summons you a pet... but at what cost...")
        ));
    }
}
