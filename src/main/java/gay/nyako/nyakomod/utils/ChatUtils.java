package gay.nyako.nyakomod.utils;

import eu.pb4.placeholders.api.TextParserUtils;
import gay.nyako.nyakomod.ChatPrefix;
import me.reimnop.d4f.Discord4Fabric;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

public class ChatUtils {
    public static void broadcast(MinecraftServer server, Text text, ChatPrefix prefix) {
        server.getPlayerManager().broadcast(prefix.apply(text), false);
        if (Discord4Fabric.DISCORD != null) {
            Discord4Fabric.DISCORD.sendPlainMessage(prefix.applyString(text));
        }
    }

    public static void broadcast(MinecraftServer server, String text, ChatPrefix prefix) {
        broadcast(server, TextParserUtils.formatText(text), prefix);
    }

    public static void send(PlayerEntity player, Text text, ChatPrefix prefix) {
        player.sendMessage(prefix.apply(text), false);
    }

    public static void send(PlayerEntity player, String text, ChatPrefix prefix) {
        send(player, TextParserUtils.formatText(text), prefix);
    }
}
