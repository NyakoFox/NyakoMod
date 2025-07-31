package gay.nyako.nyakomod.command;

import com.mojang.brigadier.CommandDispatcher;
import gay.nyako.nyakomod.utils.ChatPrefixes;
import gay.nyako.nyakomod.CunkShop;
import gay.nyako.nyakomod.data.CondensedMatterOutcomes;
import gay.nyako.nyakomod.outcomes.OutcomeContext;
import gay.nyako.nyakomod.screens.ShopEntries;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.argument;

public final class OutcomeCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("outcome")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("id", IdentifierArgumentType.identifier())
                        .suggests(new OutcomeSuggestionProvider())
                        .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ServerPlayerEntity player = source.getPlayerOrThrow();
                    ServerWorld world = source.getWorld();

                    var id = context.getArgument("id", Identifier.class);
                    var outcome = CondensedMatterOutcomes.get(id);
                    if (outcome == null)
                    {
                        context.getSource().sendError(ChatPrefixes.ERROR.apply("<gold>" + id.toString() + "</gold> <white>is not a valid outcome.</white>"));
                        return 1;
                    }

                    outcome.apply(new OutcomeContext(player, world, player.getBlockPos()));
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
