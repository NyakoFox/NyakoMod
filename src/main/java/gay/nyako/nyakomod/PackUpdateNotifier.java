package gay.nyako.nyakomod;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.UUID;

public class PackUpdateNotifier {
    static HashMap<UUID, Boolean> notifAck = new HashMap<>();

    public static void causePackUpdate(ServerPlayerEntity initiator) {
        notifyAckedPlayers(initiator);

        notifAck.replaceAll((uuid, status) -> false);
    }

    public static void notifyAckedPlayers(ServerPlayerEntity initiator) {
        for (ServerPlayerEntity player : initiator.server.getPlayerManager().getPlayerList()) {
            if (player == initiator) continue;

            if (playerHasUpdated(player)) {
                notifyPlayer(player);
            }
        }
    }

    public static void notifyPlayers() {
//        for (var player : ) {
//            if (!playerHasUpdated(player)) {
//                notifyPlayer(player);
//            }
//        }
    }

    public static void notifyPlayer(ServerPlayerEntity player) {
        player.sendMessage(Text.literal("The server resource pack has updated! Type §6/icon update§f to update."));
    }

    public static boolean playerHasUpdated(ServerPlayerEntity player) {
        return false;
    }

    public static void registerUpdate(ServerPlayerEntity player) {
        if (!notifAck.containsKey(player.getUuid())) {
            notifAck.put(player.getUuid(), true);
        } else {
            notifAck.replace(player.getUuid(), true);
        }
    }
}