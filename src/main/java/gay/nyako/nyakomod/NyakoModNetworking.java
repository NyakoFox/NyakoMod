package gay.nyako.nyakomod;

import gay.nyako.nyakomod.screens.ShopEntries;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class NyakoModNetworking {
    // Killbind packet
    public static final Identifier KILL_PLAYER_PACKET_ID = new Identifier("nyakomod", "killplayer");
    // Purchase packet
    public static final Identifier CUNK_SHOP_PURCHASE_PACKET_ID = new Identifier("nyakomod", "purchase");
    // Player smite packet
    public static final Identifier PLAYER_SMITE_PACKET_ID = new Identifier("nyakomod", "player_smite");
    // Setting pet sprite URLs
    public static final Identifier PET_SPRITE_SET_URL = new Identifier("nyakomod", "set_pet_sprite_custom_sprite");
    // Creating a model
    public static final Identifier MODEL_CREATE_PACKET = new Identifier("nyakomod", "create_model");

    public static void registerGlobalReceivers() {
        registerServerGlobalReceivers();
    }

    public static void registerServerGlobalReceivers() {

        // Kill bind
        ServerPlayNetworking.registerGlobalReceiver(KILL_PLAYER_PACKET_ID,
                (server, player, handler, buffer, sender) -> server.execute(() -> {
                    player.damage(DamageSource.MAGIC, 3.4028235E38F);
                }));

        // Custom Sprite Setting
        ServerPlayNetworking.registerGlobalReceiver(PET_SPRITE_SET_URL,
                (server, player, handler, buffer, sender) -> {
                    var string = buffer.readString();
                    var size = buffer.readDouble();

                    server.execute(() -> {
                        var stack = player.getMainHandStack();
                        if (!stack.isOf(NyakoModItem.PET_SPRITE_SUMMON_ITEM)) {
                            stack = player.getOffHandStack();
                            if (!stack.isOf(NyakoModItem.PET_SPRITE_SUMMON_ITEM)) {
                                return;
                            }
                        }

                        var nbt = stack.getOrCreateNbt();
                        nbt.putString("custom_sprite", string);
                        nbt.putDouble("pet_size", size);
                        stack.setNbt(nbt);
                    });
                }
        );


        ServerPlayNetworking.registerGlobalReceiver(MODEL_CREATE_PACKET,
                (server, player, handler, buffer, sender) -> {
                    var name = buffer.readString();
                    var type = buffer.readString();
                    var url = buffer.readString();

                    server.execute(() -> {
                        NyakoMod.MODEL_MANAGER.addModel(player, name, type, url);
                    });
                }
        );

        // Cunk shop purchasing
        ServerPlayNetworking.registerGlobalReceiver(CUNK_SHOP_PURCHASE_PACKET_ID,
                (server, player, handler, buffer, sender) -> {
                    var shopId = buffer.readIdentifier();
                    var entry = buffer.readInt();
                    var amount = buffer.readInt();
                    server.execute(() -> {
                        var shop = ShopEntries.getShop(shopId);
                        if (shop == null) {
                            return;
                        }
                        var currentEntry = shop.entries.get(entry);
                        var cost = currentEntry.price() * amount;

                        var count = CunkCoinUtils.countInventoryCoins(player.getInventory()) + CunkCoinUtils.countInventoryCoins(player.getEnderChestInventory());
                        if (count < cost) {
                            return;
                        }

                        CunkCoinUtils.removeCoins(player, cost);
                        player.getInventory().markDirty();
                        player.getInventory().updateItems();

                        if (player.playerScreenHandler != null) {
                            player.playerScreenHandler.sendContentUpdates();
                            player.playerScreenHandler.syncState();
                            player.playerScreenHandler.updateToClient();
                        }
                        if (player.currentScreenHandler != null) {
                            player.currentScreenHandler.sendContentUpdates();
                            player.currentScreenHandler.syncState();
                            player.currentScreenHandler.updateToClient();
                        }

                        for (ItemStack stack : currentEntry.stacks()) {
                            for (int i = 0; i < amount; i++) {
                                player.getInventory().offerOrDrop(stack.copy());
                            }
                        }
                    });
                });
    }
}
