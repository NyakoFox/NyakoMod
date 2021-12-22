package gay.nyako.nyakomod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import eu.pb4.placeholders.TextParser;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public final class RenameCommand {
  public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(CommandManager.literal("rename")
      .executes(RenameCommand::clearName)
      .then(CommandManager.argument("name", StringArgumentType.greedyString())
        .executes(RenameCommand::setName))
    );
  }

  public static int clearName(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    ServerCommandSource source = context.getSource();
    PlayerEntity player = source.getPlayer();
    ItemStack heldStack = player.getMainHandStack();
    if (heldStack.isEmpty()) {
      context.getSource().sendError(new LiteralText("You can't rename nothing!").formatted(Formatting.RED));
    } else {
      heldStack.removeCustomName();
      context.getSource().sendFeedback(new LiteralText("Your item's name has been cleared."), false);
    }
    return 1;
  }

  public static int setName(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    ServerCommandSource source = context.getSource();
    PlayerEntity player = source.getPlayer();
    ItemStack heldStack = player.getMainHandStack();
    Text newName = TextParser.parse(context.getArgument("name", String.class));
    if (heldStack.isEmpty()) {
      context.getSource().sendError(new LiteralText("You can't rename nothing!").formatted(Formatting.RED));
    } else {
      heldStack.setCustomName(newName);
      context.getSource().sendFeedback(new TranslatableText("Your item has been renamed to \"%s\".", newName), false);
    }
    return 1;
  }
  
}
