package gay.nyako.nyakomod.outcomes;

import gay.nyako.nyakomod.outcomes.fields.OutcomeField;
import gay.nyako.nyakomod.outcomes.positions.OutcomePosition;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.Vec2f;

public class CommandOutcome extends PositionedOutcome {
    private final OutcomeField<String> command;

    public CommandOutcome(OutcomePosition position, OutcomeField<String> command) {
        this.setPosition(position);
        this.command = command;
    }

    @Override
    public void apply(OutcomeContext context) {
        var source = new ServerCommandSource(CommandOutput.DUMMY, getPosition(context), Vec2f.ZERO, context.world(), 4, context.player().getName().getString(), context.player().getDisplayName(), context.player().getWorld().getServer(), context.player());

        context.world().getServer().getCommandManager().executeWithPrefix(source, command.get(context));
    }
}
