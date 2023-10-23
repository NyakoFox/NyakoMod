package gay.nyako.nyakomod.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.struct.PlayerTeleportPayload;

import static net.minecraft.server.command.CommandManager.literal;

import java.util.HashMap;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public final class XpCommand implements Command<ServerCommandSource> {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("xpreset")
                // .requires(Permissions.require("nyakomod.command.xpreset"))
                .executes(new XpCommand()));
    }

    @Override
    public int run(CommandContext<ServerCommandSource> ctx) {
        try {
            var player = ctx.getSource().getPlayerOrThrow();
            XpCommand.refreshLevels(player);
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendFeedback(Text.of("Only players can use this command >:("), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    public static void refreshLevels(ServerPlayerEntity player) {
    player.addExperience(0);
  }
}
