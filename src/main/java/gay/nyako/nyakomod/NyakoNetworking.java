package gay.nyako.nyakomod;

import gay.nyako.nyakomod.block.NoteBlockPlusBlockEntity;
import gay.nyako.nyakomod.entity.MonitorEntity;
import gay.nyako.nyakomod.screens.ShopEntries;
import gay.nyako.nyakomod.utils.CunkCoinUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.core.jmx.Server;

import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class NyakoNetworking {
    // Killbind packet
    public static final Identifier KILL_PLAYER_PACKET_ID = new Identifier("nyakomod", "killplayer");
    // Purchase packet
    public static final Identifier CUNK_SHOP_PURCHASE_PACKET_ID = new Identifier("nyakomod", "purchase");
    // Player smite packet
    public static final Identifier PLAYER_SMITE_PACKET_ID = new Identifier("nyakomod", "player_smite");
    // Setting pet sprite URLs
    public static final Identifier PET_SPRITE_SET_URL = new Identifier("nyakomod", "set_pet_sprite_custom_sprite");
    // Setting monitor URLs
    public static final Identifier MONITOR_SET_URL = new Identifier("nyakomod", "set_monitor_sprite");
    public static final Identifier MONITOR_MOVE = new Identifier("nyakomod", "monitor_move");
    // Creating a model
    public static final Identifier MODEL_CREATE_PACKET = new Identifier("nyakomod", "create_model");
    public static final Identifier NOTE_BLOCK_PLUS_SAVE_PACKET = new Identifier("nyakomod", "note_block_plus_save");

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
                        if (!stack.isOf(NyakoItems.PET_SPRITE_SUMMON_ITEM)) {
                            stack = player.getOffHandStack();
                            if (!stack.isOf(NyakoItems.PET_SPRITE_SUMMON_ITEM)) {
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

        ServerPlayNetworking.registerGlobalReceiver(MONITOR_SET_URL,
                (server, player, handler, buffer, sender) -> {
                    var string = buffer.readString();
                    var UUID = buffer.readUuid();
                    var width = buffer.readInt();
                    var height = buffer.readInt();

                    server.execute(() -> {
                        ServerWorld world = (ServerWorld) player.world;
                        var entity = world.getEntity(UUID);

                        if (entity instanceof MonitorEntity monitorEntity) {
                            monitorEntity.setURL(string);
                            monitorEntity.setMonitorWidth(width);
                            monitorEntity.setMonitorHeight(height);
                        }
                    });
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(MONITOR_MOVE, (server, player, handler, buffer, sender) -> {
            var UUID = buffer.readUuid();
            var offX = buffer.readDouble();
            var offY = buffer.readDouble();
            var offZ = buffer.readDouble();

            server.execute(() -> {
                ServerWorld world = (ServerWorld) player.world;
                var entity = world.getEntity(UUID);

                if (entity instanceof MonitorEntity monitorEntity) {
                    monitorEntity.setPosition(monitorEntity.getPos().add(offX, offY, offZ));
                }
            });
        });

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

        ServerPlayNetworking.registerGlobalReceiver(NOTE_BLOCK_PLUS_SAVE_PACKET,
                (server, player, handler, buffer, sender) -> {
                    var blockPos = buffer.readBlockPos();
                    var url = buffer.readString();


                    server.execute(() -> {
                        if (!player.world.isOutOfHeightLimit(blockPos)) {
                            var blockEntity = player.world.getBlockEntity(blockPos);

                            if (blockEntity instanceof NoteBlockPlusBlockEntity noteEntity) {
                                var contents = downloadFile(url);
                                if (contents != null && contents.startsWith("X:")) {
                                    noteEntity.songContents = contents;
                                    noteEntity.markDirty();
                                }
                            }
                        }
                    });
                }
        );
    }

    public static String downloadFile(String urlPath) {
        URL url = null;

        try {
            url = new URL(urlPath);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "NyakoMod");
            connection.connect();
            return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
