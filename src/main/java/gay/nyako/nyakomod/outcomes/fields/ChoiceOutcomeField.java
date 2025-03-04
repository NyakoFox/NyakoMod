package gay.nyako.nyakomod.outcomes.fields;

import gay.nyako.nyakomod.outcomes.OutcomeContext;

import java.util.List;

public class ChoiceOutcomeField<T> extends OutcomeField<T> {
    private final List<OutcomeField<T>> choices;

    public ChoiceOutcomeField(List<OutcomeField<T>> choices) {
        this.choices = choices;
    }

    @Override
    public T get(OutcomeContext context) {
        return choices.get(context.world().getRandom().nextInt(choices.size())).get(context);
    }
}
