package gay.nyako.nyakomod.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import gay.nyako.nyakomod.data.CondensedMatterOutcomes;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class OutcomeSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();

        var allIdentifiers = CondensedMatterOutcomes.getAllIdentifiers();

        // Add all player names to the builder.
        for (Identifier id : allIdentifiers) {
            builder.suggest(id.toString());
        }

        // Lock the suggestions after we've modified them.
        return builder.buildFuture();
    }
}