package gay.nyako.nyakomod.outcomes;

import gay.nyako.nyakomod.data.CondensedMatterOutcome;
import gay.nyako.nyakomod.outcomes.fields.OutcomeField;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class EffectOutcome extends CondensedMatterOutcome {
    private final OutcomeField<Identifier> effect;
    private final OutcomeField<Integer> duration;
    private final OutcomeField<Integer> amplifier;
    private final boolean showParticles;
    private final boolean showIcon;

    public EffectOutcome(OutcomeField<Identifier> effect, OutcomeField<Integer> duration, OutcomeField<Integer> amplifier) {
        super();
        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
        this.showParticles = true;
        this.showIcon = true;
    }

    public EffectOutcome(OutcomeField<Identifier> effect, OutcomeField<Integer> duration, OutcomeField<Integer> amplifier, boolean showParticles, boolean showIcon) {
        super();
        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
        this.showParticles = showParticles;
        this.showIcon = showIcon;
    }

    @Override
    public void apply(OutcomeContext context) {
        StatusEffect effect = Registries.STATUS_EFFECT.get(this.effect.get(context));
        if (effect == null)
        {
            return;
        }

        context.player().addStatusEffect(new StatusEffectInstance(effect, duration.get(context), amplifier.get(context), true, showParticles, showIcon));
    }
}
