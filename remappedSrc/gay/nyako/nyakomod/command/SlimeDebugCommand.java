package gay.nyako.nyakomod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;

public final class SlimeDebugCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(CommandManager.literal("slimedebug")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("ticks", IntegerArgumentType.integer()).executes(context -> {
                    NyakoMod.SLIME_SKY_MANAGER.stateLength = IntegerArgumentType.getInteger(context, "ticks");
                    return 0;
                }))
        );
    }
}
