package gay.nyako.nyakomod.outcomes.positions;

import gay.nyako.nyakomod.outcomes.OutcomeContext;
import gay.nyako.nyakomod.outcomes.fields.Vec3OutcomeNumber;
import net.minecraft.util.math.Vec3d;

public class PlayerOutcomePosition extends OutcomePosition {
    public PlayerOutcomePosition(Vec3OutcomeNumber offset) {
        super(offset);
    }

    @Override
    public Vec3d getPosition(OutcomeContext context) {
        return context.player().getPos().add(offset.get(context));
    }
}
