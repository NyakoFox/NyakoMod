package gay.nyako.nyakomod;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.util.Util;

import java.util.List;
import java.util.function.Consumer;

public class NyakoAdvancementProvider extends FabricAdvancementProvider {

    private final List<Consumer<Consumer<AdvancementEntry>>> generators = Util.make(Lists.newArrayList(), list -> {
        list.add(new NyakoAdvancementsGenerator());
    });

    protected NyakoAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<AdvancementEntry> consumer) {
        generators.forEach(i -> i.accept(consumer));
    }
}