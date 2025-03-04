package gay.nyako.nyakomod.outcomes;

import gay.nyako.nyakomod.CondensedMatterOutcomeContainer;
import gay.nyako.nyakomod.data.CondensedMatterOutcome;
import gay.nyako.nyakomod.outcomes.fields.OutcomeField;

public class RepeatOutcome extends CondensedMatterOutcome {
    private final OutcomeField<Integer> count;
    private final CondensedMatterOutcomeContainer outcomes;

    public RepeatOutcome(OutcomeField<Integer> count, CondensedMatterOutcomeContainer outcomes) {
        super();
        this.count = count;
        this.outcomes = outcomes;
    }

    @Override
    public void apply(OutcomeContext context) {
        int count = this.count.get(context);
        for (int i = 0; i < count; i++) {
            outcomes.apply(context);
        }
    }
}
