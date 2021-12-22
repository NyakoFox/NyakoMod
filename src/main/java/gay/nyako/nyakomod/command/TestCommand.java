package gay.nyako.nyakomod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.struct.PlayerTeleportPayload;
import io.netty.buffer.Unpooled;

import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public final class TestCommand {
  public static HashMap<Integer, PlayerTeleportPayload> previousLocations = new HashMap<>();

  public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(CommandManager.literal("test")
					.then(CommandManager.argument("name", StringArgumentType.string())
						.then(CommandManager.argument("url", StringArgumentType.greedyString())
								.executes(TestCommand::run)
						)
					)
			);
  }

  public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    ServerCommandSource source = context.getSource();
    PlayerEntity player = source.getPlayer();
    ItemStack heldStack = player.getMainHandStack();
    String inputName = context.getArgument("name", String.class);
    String input = context.getArgument("url", String.class);

    BufferedImage image = null;
    URL url = null;

    try {
      url = new URL(input);
      URLConnection connection = url.openConnection();
      connection.setRequestProperty("User-Agent", "NyakoMod");
      connection.connect();
      image = ImageIO.read(connection.getInputStream());
    } catch (Exception e) {
      url = null;
      image = null;
    }

    if (image == null) {
      context.getSource().sendError(new LiteralText("Cringe URL").formatted(Formatting.RED));
      return 1;
    }

    // Our checks passed (valid URL and valid image) so let's send it to the client

    // Actually first we'll set the custom model id
    NbtCompound nbt = heldStack.getOrCreateNbt();
    nbt.putString("modelId", "nyakomod_custom:" + inputName);

    // Build the packet

    PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
    passedData.writeString(input);
    passedData.writeIdentifier(new Identifier("nyakomod_custom", inputName));

    // Then we'll send the packet to all the players
    MinecraftServer server = source.getServer();
    PlayerManager playerManager = server.getPlayerManager();
    List<ServerPlayerEntity> playerList = playerManager.getPlayerList();

    playerList.forEach(currentPlayer ->
      ServerPlayNetworking.send(currentPlayer, NyakoMod.IMAGE_DOWNLOAD_PACKET_ID, passedData));

    return 1;
  }
}
