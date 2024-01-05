package gay.nyako.nyakomod;

import net.minecraft.block.SaplingGenerator;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;

import java.util.Optional;

public class NyakoSaplingGenerators {
    public static final SaplingGenerator ECHO = new SaplingGenerator("echo", Optional.of(NyakoTreeConfiguredFeatures.ECHO), Optional.empty(), Optional.empty());
    public static final SaplingGenerator BENTHIC = new SaplingGenerator("benthic", Optional.of(NyakoTreeConfiguredFeatures.BENTHIC), Optional.empty(), Optional.empty());
}
