package gay.nyako.nyakomod.outcomes.fields;

import gay.nyako.nyakomod.outcomes.OutcomeContext;

public class RangeOutcomeNumber extends OutcomeField<Double> {
    private final OutcomeField<Double> min;
    private final OutcomeField<Double> max;

    public RangeOutcomeNumber(double min, double max) {
        super();
        this.min = new ConstantOutcomeField<>(min);
        this.max = new ConstantOutcomeField<>(max);
    }

    public RangeOutcomeNumber(OutcomeField<Double> min, OutcomeField<Double> max) {
        super();
        this.min = min;
        this.max = max;
    }

    @Override
    public Double get(OutcomeContext context) {
        var randomDouble = context.world().getRandom().nextDouble();
        return min.get(context) + (max.get(context) - min.get(context)) * randomDouble;
    }
}
