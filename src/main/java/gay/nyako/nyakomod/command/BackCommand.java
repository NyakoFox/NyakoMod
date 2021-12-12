package gay.nyako.nyakomod.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import gay.nyako.nyakomod.mixin.ServerPlayNetworkHandlerMixin;
import gay.nyako.nyakomod.struct.PlayerTeleportPayload;
import me.lucko.fabric.api.permissions.v0.Permissions;

import static net.minecraft.server.command.CommandManager.literal;

import java.util.HashMap;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public final class BackCommand implements Command<ServerCommandSource> {
  public static HashMap<Integer, PlayerTeleportPayload> previousLocations = new HashMap<>();

  public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(literal("back")
      // .requires(Permissions.require("nyakomod.command.back"))
      .executes(new BackCommand()));
  }

  @Override
  public int run(CommandContext<ServerCommandSource> ctx) {
    var source = ctx.getSource();
    try {
      var player = source.getPlayer();
      var id = player.getId();

      var previousLocation = previousLocations.get(id);

      player.requestTeleport(previousLocation.x, previousLocation.y, previousLocation.z);
      ctx.getSource().sendFeedback(Text.of("Woo!"), false);
    } catch (CommandSyntaxException e) {
      ctx.getSource().sendFeedback(Text.of("Only players can use this command >:("), false);
    }

    return Command.SINGLE_SUCCESS;
  }
}
