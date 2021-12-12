package gay.nyako.nyakomod.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import gay.nyako.nyakomod.struct.PlayerTeleportPayload;

import static net.minecraft.server.command.CommandManager.literal;

import java.util.HashMap;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public final class BackCommand implements Command<ServerCommandSource> {
  public static HashMap<Integer, PlayerTeleportPayload> previousLocations = new HashMap<>();

  public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(literal("back")
      // .requires(Permissions.require("nyakomod.command.back"))
      .executes(new BackCommand()));
  }

  public static void registerPreviousLocation(ServerPlayerEntity p) {
    System.out.println("Registering previous location");
    var payload = new PlayerTeleportPayload() {
      {
        player = p;
        world = p.getWorld();
        x = p.getX();
        y = p.getY();
        z = p.getZ();
        yaw = p.getYaw();
        pitch = p.getPitch();
      }
    };

    int id = p.getId();

    if (BackCommand.previousLocations.containsKey(id)) {
      BackCommand.previousLocations.replace(id, payload);
    } else {
      BackCommand.previousLocations.put(id, payload);
    }
  }

  @Override
  public int run(CommandContext<ServerCommandSource> ctx) {
    var source = ctx.getSource();
    try {
      var p = source.getPlayer();
      var id = p.getId();

      var previousLocation = previousLocations.get(id);

      if (previousLocation != null) {
        var newLocation = new PlayerTeleportPayload() {
          {
            player = p;
            world = p.getWorld();
            x = p.getX();
            y = p.getY();
            z = p.getZ();
            yaw = p.getYaw();
            pitch = p.getPitch();
          }
        };

        p.teleport(previousLocation.world, previousLocation.x, 
          previousLocation.y, previousLocation.z, previousLocation.yaw, previousLocation.pitch);
        ctx.getSource().sendFeedback(Text.of("Woo!"), false);

        previousLocations.replace(id, newLocation);
      } else {
        ctx.getSource().sendFeedback(Text.of("You don't have a previous location."), false);
      }
    } catch (CommandSyntaxException e) {
      ctx.getSource().sendFeedback(Text.of("Only players can use this command >:("), false);
    }

    return Command.SINGLE_SUCCESS;
  }
}
