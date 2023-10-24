package gay.nyako.nyakomod;

import gay.nyako.nyakomod.feature.EchoDungeonFeature;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class NyakoFeatures {
    public static final Feature<DefaultFeatureConfig> ECHO_MONSTER_ROOM = register("echo_monster_room", new EchoDungeonFeature(DefaultFeatureConfig.CODEC));

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String id, F feature) {
        return Registry.register(Registries.FEATURE, new Identifier("nyakomod", id), feature);
    }

    public static void register()
    {
        // Intentionally left blank
    }
}
