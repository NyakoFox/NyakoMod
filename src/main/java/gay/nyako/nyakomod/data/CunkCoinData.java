package gay.nyako.nyakomod.data;

import net.minecraft.util.Identifier;

import java.util.HashMap;

public class CunkCoinData {
    private final long value;
    public HashMap<Identifier, Double> dimensionMultipliers;

    public CunkCoinData(long value) {
        this.value = value;
        dimensionMultipliers = new HashMap<>();
    }

    public long getValue() {
        return value;
    }

    public void addMultiplier(Identifier dimension, double multiplier) {
        dimensionMultipliers.put(dimension, multiplier);
    }

    public double getMultiplier(Identifier dimension) {
        return dimensionMultipliers.getOrDefault(dimension, 1.0);
    }
}
