package gay.nyako.nyakomod;

import eu.pb4.placeholders.api.TextParserUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;

import java.util.HashMap;
import java.util.UUID;

public class PackUpdateNotifier {
    static HashMap<UUID, Boolean> notifAck = new HashMap<>();

    public static void causePackUpdate(ServerPlayerEntity initiator) {
        notifyAckedPlayers(initiator);

        notifAck.replaceAll((uuid, status) -> uuid.equals(initiator.getUuid()));
    }

    public static void notifyAckedPlayers(ServerPlayerEntity initiator) {
        for (ServerPlayerEntity player : initiator.server.getPlayerManager().getPlayerList()) {
            if (player == initiator) continue;

            if (playerHasUpdated(player)) {
                notifyPlayer(player);
            }
        }
    }

    public static void notifyUnackedPlayers(MinecraftServer server) {
        for (var player : server.getPlayerManager().getPlayerList()) {
            if (!playerHasUpdated(player)) {
                notifyPlayer(player);
            }
        }
    }

    public static void notifyPlayer(ServerPlayerEntity player) {
        var text = Text.literal("[HERE]").formatted(Formatting.GOLD);
        text.setStyle(text.getStyle()
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pack update"))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.of("/pack update")))
                .withBold(true));
        var prefix = (MutableText)TextParserUtils.formatText("<aqua>[i]</aqua> <bold>>></bold> ");
        player.sendMessage(
                prefix.append("The server resource pack has updated! Click ")
                        .append(text)
                        .append(" to update."));
    }

    public static boolean playerHasUpdated(ServerPlayerEntity player) {
        return notifAck.getOrDefault(player.getUuid(), false);
    }

    public static void registerUpdate(ServerPlayerEntity player) {
        if (!notifAck.containsKey(player.getUuid())) {
            notifAck.put(player.getUuid(), true);
        } else {
            notifAck.replace(player.getUuid(), true);
        }
    }
}