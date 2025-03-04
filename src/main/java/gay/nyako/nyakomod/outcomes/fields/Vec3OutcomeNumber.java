package gay.nyako.nyakomod.outcomes.fields;

import gay.nyako.nyakomod.outcomes.OutcomeContext;
import net.minecraft.util.math.Vec3d;

public class Vec3OutcomeNumber {
    private final OutcomeField<Double> x;
    private final OutcomeField<Double> y;
    private final OutcomeField<Double> z;

    public Vec3OutcomeNumber(OutcomeField<Double> x, OutcomeField<Double> y, OutcomeField<Double> z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3OutcomeNumber(double x, double y, double z) {
        this(new ConstantOutcomeField<>(x), new ConstantOutcomeField<>(y), new ConstantOutcomeField<>(z));
    }

    public Vec3d get(OutcomeContext context) {
        return new Vec3d(x.get(context), y.get(context), z.get(context));
    }
}
