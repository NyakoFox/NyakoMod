package gay.nyako.nyakomod.screens;

import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.NyakoModPotion;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ShopEntries {
    public static List<ShopData> shops = new ArrayList<>();

    public static final Identifier MAIN = new Identifier("nyakomod", "main");

    public static ShopData register(ShopData shopData) {
        shops.add(shopData);
        return shopData;
    }

    public static ShopData getShop(Identifier id) {
        for (ShopData shop : shops) {
            if (shop.id.equals(id)) {
                return shop;
            }
        }

        return null;
    }
}
