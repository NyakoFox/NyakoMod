package gay.nyako.nyakomod.outcomes.positions;

import gay.nyako.nyakomod.outcomes.OutcomeContext;
import gay.nyako.nyakomod.outcomes.fields.Vec3OutcomeNumber;
import net.minecraft.util.math.Vec3d;

public abstract class OutcomePosition {
    protected Vec3OutcomeNumber offset;

    OutcomePosition(Vec3OutcomeNumber offset)
    {
        this.offset = offset;
    }

    public abstract Vec3d getPosition(OutcomeContext context);
}
