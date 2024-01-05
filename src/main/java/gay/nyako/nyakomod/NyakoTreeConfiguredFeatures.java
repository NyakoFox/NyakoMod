package gay.nyako.nyakomod;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;

public class NyakoTreeConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> ECHO = ConfiguredFeatures.of("nyakomod:echotree_basic");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BENTHIC = ConfiguredFeatures.of("nyakomod:benthictree_basic");
}
