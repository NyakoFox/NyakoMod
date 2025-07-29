package gay.nyako.nyakomod.screens;

import com.google.gson.JsonObject;
import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopEntries {
    public static List<ShopData> shops = new ArrayList<>();

    public static HashMap<Identifier, JsonObject> savedJson = new HashMap<>();

    public static final Identifier MAIN = NyakoMod.id("main");

    public static ShopData register(ShopData shopData) {
        var oldShop = getShop(shopData.id);
        if (oldShop != null) {
            shops.remove(oldShop);
        }

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
