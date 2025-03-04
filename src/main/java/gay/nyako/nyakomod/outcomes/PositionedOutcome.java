package gay.nyako.nyakomod.outcomes;

import gay.nyako.nyakomod.data.CondensedMatterOutcome;
import gay.nyako.nyakomod.outcomes.positions.OutcomePosition;
import net.minecraft.util.math.Vec3d;

public abstract class PositionedOutcome extends CondensedMatterOutcome {
    private OutcomePosition position;

    public void setPosition(OutcomePosition position)
    {
        this.position = position;
    }

    public Vec3d getPosition(OutcomeContext context) {
        return position.getPosition(context);
    }
}
