package gay.nyako.nyakomod;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class NyakoDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        // ...
        fabricDataGenerator.addProvider(NyakoModelGenerator::new);
        fabricDataGenerator.addProvider(NyakoBlockLootTableProvider::new);
        fabricDataGenerator.addProvider(NyakoAdvancementProvider::new);
        // ...
    }
}