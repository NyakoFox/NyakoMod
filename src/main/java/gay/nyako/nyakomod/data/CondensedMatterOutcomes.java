package gay.nyako.nyakomod.data;

import gay.nyako.nyakomod.CondensedMatterOutcomeContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

import java.util.HashMap;

public class CondensedMatterOutcomes {
    public static HashMap<Identifier, CondensedMatterOutcomeContainer> outcomes = new HashMap<>();

    public static void add(Identifier id, CondensedMatterOutcomeContainer outcome) {
        outcomes.put(id, outcome);
    }

    public static void clear() {
        outcomes.clear();
    }

    public static CondensedMatterOutcomeContainer get(Identifier id) {
        return outcomes.get(id);
    }

    public static CondensedMatterOutcomeContainer random(Random random) {
        var values = outcomes.values().toArray();
        return (CondensedMatterOutcomeContainer) values[random.nextInt(values.length)];
    }

    public static Identifier[] getAllIdentifiers() {
        return outcomes.keySet().toArray(new Identifier[0]);
    }
}
