package gay.nyako.nyakomod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import eu.pb4.placeholders.api.TextParserUtils;
import gay.nyako.nyakomod.CachedResourcePack;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.PackUpdateNotifier;
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

public final class PackCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("pack")
                .then(CommandManager.literal("update").executes(PackCommand::reloadCommand))
                .then(CommandManager.literal("publish").executes(PackCommand::publishCommand))
                .then(CommandManager.literal("remind").executes(PackCommand::remindCommand)));
    }

    public static int reloadCommand(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        CachedResourcePack.setPlayerResourcePack(source.getPlayer());
        return 1;
    }

    public static int publishCommand(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        NyakoMod.CACHED_RESOURCE_PACK.zipResourcePack();
        NyakoMod.CACHED_RESOURCE_PACK.cacheResourcePack();
        PackUpdateNotifier.causePackUpdate(source.getPlayer());
        source.sendFeedback(() -> TextParserUtils.formatText("<green>[✔]</green> <bold>>></bold> Published the pack!"), false);
        return 1;
    }

    public static int remindCommand(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        PackUpdateNotifier.notifyUnackedPlayers(source.getServer());
        return 1;
    }
}
