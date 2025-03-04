package gay.nyako.nyakomod.outcomes.positions;

import gay.nyako.nyakomod.outcomes.OutcomeContext;
import gay.nyako.nyakomod.outcomes.fields.Vec3OutcomeNumber;
import net.minecraft.util.math.Vec3d;

public class BlockOutcomePosition extends OutcomePosition {
    public BlockOutcomePosition() {
        super(new Vec3OutcomeNumber(0, 0, 0));
    }

    public BlockOutcomePosition(Vec3OutcomeNumber offset) {
        super(offset);
    }

    @Override
    public Vec3d getPosition(OutcomeContext context) {
        return context.blockPos().toCenterPos().add(offset.get(context));
    }
}
