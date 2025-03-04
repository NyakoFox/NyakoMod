package gay.nyako.nyakomod.outcomes.fields;

import gay.nyako.nyakomod.outcomes.OutcomeContext;

public class RangeOutcomeInteger extends OutcomeField<Integer> {
    private final OutcomeField<Integer> min;
    private final OutcomeField<Integer> max;

    public RangeOutcomeInteger(int min, int max) {
        super();
        this.min = new ConstantOutcomeField<>(min);
        this.max = new ConstantOutcomeField<>(max);
    }

    public RangeOutcomeInteger(OutcomeField<Integer> min, OutcomeField<Integer> max) {
        super();
        this.min = min;
        this.max = max;
    }

    @Override
    public Integer get(OutcomeContext context) {
        return context.world().getRandom().nextBetween(min.get(context), max.get(context));
    }
}
