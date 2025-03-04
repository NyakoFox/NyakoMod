package gay.nyako.nyakomod.outcomes;

import gay.nyako.nyakomod.outcomes.fields.OutcomeField;
import gay.nyako.nyakomod.outcomes.positions.OutcomePosition;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;

public class FunctionOutcome extends PositionedOutcome {
    private final OutcomeField<Identifier> function;

    public FunctionOutcome(OutcomePosition position, OutcomeField<Identifier> function) {
        this.setPosition(position);
        this.function = function;
    }

    @Override
    public void apply(OutcomeContext context) {
        var function = context.world().getServer().getCommandFunctionManager().getFunction(this.function.get(context));
        if (function.isEmpty()) {
            return;
        }

        var source = new ServerCommandSource(CommandOutput.DUMMY, getPosition(context), Vec2f.ZERO, context.world(), 4, context.player().getName().getString(), context.player().getDisplayName(), context.player().getWorld().getServer(), context.player());

        context.world().getServer().getCommandFunctionManager().execute(function.get(), source);
    }
}
