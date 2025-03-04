package gay.nyako.nyakomod.outcomes.positions;

import gay.nyako.nyakomod.outcomes.OutcomeContext;
import gay.nyako.nyakomod.outcomes.fields.Vec3OutcomeNumber;
import net.minecraft.util.math.Vec3d;

public class AbsoluteOutcomePosition extends OutcomePosition {
    public AbsoluteOutcomePosition(Vec3OutcomeNumber offset) {
        super(offset);
    }

    @Override
    public Vec3d getPosition(OutcomeContext context) {
        return offset.get(context);
    }
}
