package gay.nyako.nyakomod;

import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import gay.nyako.nyakomod.screens.CunkShopScreenHandler;
import gay.nyako.nyakomod.screens.ShopData;
import gay.nyako.nyakomod.screens.ShopEntries;
import gay.nyako.nyakomod.screens.ShopEntry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CunkShop {
    public static void openShop(PlayerEntity player, World world, Identifier shop) {
        player.openHandledScreen(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                buf.writeIdentifier(shop);
                var string = ShopEntries.savedJson.get(shop).toString();
                var length = string.getBytes(StandardCharsets.UTF_8).length;
                buf.writeInt(length);
                buf.writeString(string, length);
            }

            @Override
            public Text getDisplayName() {
                return Text.literal("Icon Selector");
            }

            @Override
            public @NotNull ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                return new CunkShopScreenHandler(syncId, inv, ScreenHandlerContext.create(world, player.getBlockPos()));
            }
        });
    }

    public static void storeShopModelJson(JsonObject shopJson, Identifier shop) {
        ShopEntries.savedJson.put(shop, shopJson);
    }

    public static void loadShopModelFromJson(JsonObject shopJson, ShopData shopData) {
        shopJson.getAsJsonArray("entries").forEach(entry -> {
            JsonObject entryJson = entry.getAsJsonObject();
            var stacks = new ArrayList<ItemStack>();
            entryJson.getAsJsonArray("items").forEach(item -> {
                var jsonObject = item.getAsJsonObject();
                if (jsonObject.has("nbt")) {
                    var snbt = jsonObject.get("nbt").getAsString();
                    var stack = new ItemStack(Registry.ITEM.get(new Identifier(jsonObject.get("id").getAsString())));
                    if (jsonObject.has("count")) {
                        stack.setCount(jsonObject.get("count").getAsInt());
                    } else if (jsonObject.has("Count")) {
                        stack.setCount(jsonObject.get("Count").getAsInt());
                    }
                    try {
                        stack.setNbt(NbtHelper.fromNbtProviderString(snbt));
                    } catch (CommandSyntaxException e) {
                        e.printStackTrace();
                    }
                    stacks.add(stack);
                } else {
                    // Convert Name from json to a string
                    // Lore needs something like this as well
                    /*if ((jsonObject.get("tag") != null) &&
                            (jsonObject.get("tag").getAsJsonObject().get("display") != null) &&
                            (jsonObject.get("tag").getAsJsonObject().get("display").getAsJsonObject().get("Name") != null)) {
                        var display = jsonObject.get("tag").getAsJsonObject().get("display").getAsJsonObject();
                        var string = gson.toJson(display.get("Name"));
                        display.addProperty("Name",  string);
                    }*/
                    NbtCompound converted = (NbtCompound) Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, jsonObject);
                    stacks.add(ItemStack.fromNbt(converted));
                }
            });
            shopData.add(
                    new ShopEntry(
                            stacks,
                            entryJson.get("price").getAsInt(),
                            Text.of(entryJson.get("name").getAsString()),
                            Text.of(entryJson.get("description").getAsString())
                    )
            );
        });

        ShopEntries.register(shopData);
    }
}
