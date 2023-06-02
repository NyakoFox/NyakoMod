package gay.nyako.nyakomod.utils;

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import eu.pb4.placeholders.api.TextParserUtils;
import gay.nyako.nyakomod.ChatPrefix;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

public class ChatUtils {
    public static void broadcast(MinecraftServer server, Text text, ChatPrefix prefix) {
        var text2 = Placeholders.parseText(text, PlaceholderContext.of(server));
        server.getPlayerManager().broadcast(prefix.apply(text2), false);
    }

    public static void broadcast(MinecraftServer server, String text, ChatPrefix prefix) {
        broadcast(server, TextParserUtils.formatText(text), prefix);
    }

    public static void send(PlayerEntity player, Text text, ChatPrefix prefix) {
        var text2 = Placeholders.parseText(text, PlaceholderContext.of(player));
        player.sendMessage(prefix.apply(text2), false);
    }

    public static void send(PlayerEntity player, String text, ChatPrefix prefix) {
        send(player, TextParserUtils.formatText(text), prefix);
    }
}
