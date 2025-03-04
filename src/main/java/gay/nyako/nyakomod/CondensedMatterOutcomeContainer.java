package gay.nyako.nyakomod;

import gay.nyako.nyakomod.data.CondensedMatterOutcome;
import gay.nyako.nyakomod.outcomes.OutcomeContext;

import java.util.ArrayList;
import java.util.List;

public class CondensedMatterOutcomeContainer {
    public List<CondensedMatterOutcome> outcomes = new ArrayList<>();

    public void add(CondensedMatterOutcome outcome) {
        outcomes.add(outcome);
    }

    public void clear() {
        outcomes.clear();
    }

    public void apply(OutcomeContext context) {
        for (var outcome : outcomes) {
            outcome.apply(context);
        }
    }
}
