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

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class LoreCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("lore")
                .then(CommandManager.literal("clear").executes(LoreCommand::clearLore))
                .then(CommandManager.literal("add").then(argument("text", StringArgumentType.greedyString()).executes(LoreCommand::addLore)))
        );
    }

    public static int clearLore(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        PlayerEntity player = source.getPlayer();
        ItemStack heldStack = player.getMainHandStack();
        if (heldStack.isEmpty()) {
            context.getSource().sendError(TextParserUtils.formatText("<red>[❌]</red> <bold>>></bold> You can't clear the lore of nothing."));
        } else {
            NbtCompound nbt = heldStack.getOrCreateNbt();
            NbtCompound nbtDisplay = nbt.getCompound(ItemStack.DISPLAY_KEY);
            NbtList nbtLore = new NbtList();

            nbtDisplay.put(ItemStack.LORE_KEY, nbtLore);
            nbt.put(ItemStack.DISPLAY_KEY, nbtDisplay);
            heldStack.setNbt(nbt);
            context.getSource().sendFeedback(TextParserUtils.formatText("<green>[✔]</green> <bold>>></bold> Lore cleared."), false);
        }
        return 1;
    }

    public static int addLore(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        PlayerEntity player = source.getPlayer();
        ItemStack heldStack = player.getMainHandStack();
        Text newText = TextParserUtils.formatText(context.getArgument("text", String.class));
        if (heldStack.isEmpty()) {
            context.getSource().sendError(TextParserUtils.formatText("<red>[❌]</red> <bold>>></bold> You can't add lore to nothing."));
        } else {
            NbtCompound nbt = heldStack.getOrCreateNbt();
            NbtCompound nbtDisplay = nbt.getCompound(ItemStack.DISPLAY_KEY);
            NbtList nbtLore = nbtDisplay.getList(ItemStack.LORE_KEY, NbtElement.STRING_TYPE);

            nbtLore.add(NbtString.of(Text.Serializer.toJson(newText)));

            nbtDisplay.put(ItemStack.LORE_KEY, nbtLore);
            nbt.put(ItemStack.DISPLAY_KEY, nbtDisplay);
            heldStack.setNbt(nbt);
            context.getSource().sendFeedback(TextParserUtils.formatText("<green>[✔]</green> <bold>>></bold> Lore applied."), false);
        }
        return 1;
    }
}
