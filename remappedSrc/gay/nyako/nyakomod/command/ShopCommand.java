package gay.nyako.nyakomod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import gay.nyako.nyakomod.CunkShop;
import gay.nyako.nyakomod.screens.ShopEntries;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.argument;

public final class ShopCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("shop")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("name", StringArgumentType.greedyString()).executes(context -> {
                    ServerCommandSource source = context.getSource();
                    PlayerEntity player = source.getPlayerOrThrow();
                    ServerWorld world = source.getWorld();

                    CunkShop.openShop(player, world, new Identifier("nyakomod", context.getArgument("name", String.class)));
                    return 0;
                }))
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    PlayerEntity player = source.getPlayerOrThrow();
                    ServerWorld world = source.getWorld();

                    CunkShop.openShop(player, world, ShopEntries.MAIN);

                    return 0;
                })
        );
    }
}
