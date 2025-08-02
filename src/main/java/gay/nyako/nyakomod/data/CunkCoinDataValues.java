package gay.nyako.nyakomod.data;

import net.minecraft.util.Identifier;

import java.util.HashMap;

public class CunkCoinDataValues {
    public static HashMap<Identifier, CunkCoinData> cunkCoinMap = new HashMap<>();

    public static void add(Identifier id, CunkCoinData outcome) {
        cunkCoinMap.put(id, outcome);
    }

    public static void clear() {
        cunkCoinMap.clear();
    }

    public static CunkCoinData get(Identifier id) {
        return cunkCoinMap.get(id);
    }
}
