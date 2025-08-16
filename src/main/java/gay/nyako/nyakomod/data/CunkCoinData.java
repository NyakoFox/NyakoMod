package gay.nyako.nyakomod.data;

import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CunkCoinData {
    private final long value;
    public HashMap<Identifier, Double> dimensionMultipliers;
    public List<NBTMultiplierCondition> nbtConditions;

    public CunkCoinData(long value) {
        this.value = value;
        dimensionMultipliers = new HashMap<>();
        nbtConditions = new ArrayList<>();
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

    public void addNBTCondition(String key, String value, double multiplier) {
        nbtConditions.add(new NBTMultiplierCondition(key, value, multiplier));
    }

    public List<NBTMultiplierCondition> getNBTConditions() {
        return nbtConditions;
    }

    public record NBTMultiplierCondition(String key, String value, double multiplier) {
    }
}
