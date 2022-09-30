package gay.nyako.nyakomod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import eu.pb4.placeholders.api.TextParserUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;

public final class RenameCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("rename")
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
            context.getSource().sendError(TextParserUtils.formatText("<red>[❌]</red> <bold>>></bold> You can't rename nothing."));
        } else {
            heldStack.removeCustomName();
            context.getSource().sendFeedback(TextParserUtils.formatText("<green>[✔]</green> <bold>>></bold> Your item's name has been cleared."), false);
        }
        return 1;
    }

    public static int setName(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        PlayerEntity player = source.getPlayer();
        ItemStack heldStack = player.getMainHandStack();
        Text newName = TextParserUtils.formatText(context.getArgument("name", String.class));
        if (heldStack.isEmpty()) {
            context.getSource().sendError(TextParserUtils.formatText("<red>[❌]</red> <bold>>></bold> You can't rename nothing."));
        } else {
            heldStack.setCustomName(newName);
            context.getSource().sendFeedback(TextParserUtils.formatText("<green>[✔]</green> <bold>>></bold> Your item has been renamed to " + newName + "."), false);
        }
        return 1;
    }
}
