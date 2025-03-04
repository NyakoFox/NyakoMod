package gay.nyako.nyakomod.outcomes.fields;

import gay.nyako.nyakomod.outcomes.OutcomeContext;

public class ConstantOutcomeField<T> extends OutcomeField<T> {
    private final T value;

    public ConstantOutcomeField(T value) {
        this.value = value;
    }

    @Override
    public T get(OutcomeContext context) {
        return value;
    }
}
