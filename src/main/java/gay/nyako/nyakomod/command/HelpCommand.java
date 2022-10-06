package gay.nyako.nyakomod.command;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import eu.pb4.placeholders.api.TextParserUtils;
import gay.nyako.nyakomod.ChatPrefixes;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.Map;

public class HelpCommand {

    private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.help.failed"));
    public static final int COMMANDS_PER_PAGE = 8;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(helpCommand(dispatcher));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> helpCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        return CommandManager.literal("help")
                .executes(context -> {
                    return showPage(dispatcher, context, 1);
                })
                .then(CommandManager.argument("page", IntegerArgumentType.integer(1, 1000))
                .executes(context -> {
                    return showPage(dispatcher, context, IntegerArgumentType.getInteger(context, "page"));
                }))
                .then(CommandManager.argument("command", StringArgumentType.greedyString())
                .executes(context -> {
                    String command = StringArgumentType.getString(context, "command");
                    Integer page = null;
                    try {
                        page = Integer.parseInt(command);
                    } catch (NumberFormatException nfe) {
                        //ignore
                    }

                    if (page != null) {
                        return showPage(dispatcher, context, page);
                    }

                    ParseResults<ServerCommandSource> parseResults = dispatcher.parse(StringArgumentType.getString(context, "command"), context.getSource());
                    if (parseResults.getContext().getNodes().isEmpty()) {
                        (context.getSource()).sendError(ChatPrefixes.ERROR.apply("<gold>" + command + "</gold> <white>is not a valid command.</white>"));
                        return 0;
                    }

                    (context.getSource()).sendFeedback(ChatPrefixes.SUCCESS.apply("Showing help for <gold>" + command + "</gold>"), false);

                    Map<CommandNode<ServerCommandSource>, String> map = dispatcher.getSmartUsage(Iterables.getLast(parseResults.getContext().getNodes()).getNode(), context.getSource());
                    for (String string : map.values()) {
                        (context.getSource()).sendFeedback(ChatPrefixes.INFO.apply("/" + parseResults.getReader().getString() + " " + string), false);
                    }
                    return map.size();
                }));
    }

    private static int showPage(CommandDispatcher<ServerCommandSource> dispatcher, CommandContext<ServerCommandSource> context, int page) {
        Map<CommandNode<ServerCommandSource>, String> map = dispatcher.getSmartUsage(dispatcher.getRoot(), context.getSource());
        int totalPages = MathHelper.ceil((float) map.size() / COMMANDS_PER_PAGE);
        page = MathHelper.clamp(page, 1, totalPages);
        int commandOffset = (page - 1) * COMMANDS_PER_PAGE;
        int commandCount = Math.min(COMMANDS_PER_PAGE, map.size() - commandOffset);
        (context.getSource()).sendFeedback(ChatPrefixes.SUCCESS.apply("Showing page <gold>" + page + "</gold> of <gold>" + totalPages + "</gold>"), false);

        for (String string : map.values()) {
            if (commandOffset > 0) {
                --commandOffset;
            } else if (commandCount > 0) {
                --commandCount;
                (context.getSource()).sendFeedback(ChatPrefixes.INFO.apply("/" + string), false);
            }
        }

        return map.size();
    }

}
