package gay.nyako.nyakomod.outcomes.fields;

import gay.nyako.nyakomod.outcomes.OutcomeContext;

public abstract class OutcomeField<T> {
    public abstract T get(OutcomeContext context);
}
