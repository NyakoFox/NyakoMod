package gay.nyako.nyakomod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import eu.pb4.placeholders.api.TextParserUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class FakeCountCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("fakecount")
                .executes(FakeCountCommand::clearCount)
                .then(CommandManager.argument("count", StringArgumentType.greedyString())
                        .executes(FakeCountCommand::setCount))
        );
    }

    public static int clearCount(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        PlayerEntity player = source.getPlayer();
        if (player == null) {
            return 0;
        }
        ItemStack heldStack = player.getMainHandStack();
        if (heldStack.isEmpty()) {
            context.getSource().sendError(TextParserUtils.formatText("<red>[❌]</red> <white><bold>>></bold> You can't set the count of nothing.</white>"));
        } else {
            heldStack.removeSubNbt("customCount");
            context.getSource().sendFeedback(TextParserUtils.formatText("<green>[✔]</green> <bold>>></bold> Your item's count has been reset."), false);
        }
        return 1;
    }

    public static int setCount(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        PlayerEntity player = source.getPlayer();
        if (player == null) {
            return 0;
        }
        ItemStack heldStack = player.getMainHandStack();
        Text newCount = TextParserUtils.formatText(context.getArgument("count", String.class));
        if (heldStack.isEmpty()) {
            context.getSource().sendError(TextParserUtils.formatText("<red>[❌]</red> <white><bold>>></bold> You can't set the count of air.</white>"));
        } else {
            var nbt = heldStack.getOrCreateNbt();
            nbt.putString("customCount", newCount.getString());
            heldStack.setNbt(nbt);
            context.getSource().sendFeedback(TextParserUtils.formatText("<green>[✔]</green> <bold>>></bold> Your item now has the count of " + newCount + "."), false);
        }
        return 1;
    }
}