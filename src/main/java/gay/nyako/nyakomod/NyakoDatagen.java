package gay.nyako.nyakomod;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataProvider;

public class NyakoDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        // ...
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(NyakoModelGenerator::new);
        pack.addProvider(NyakoBlockLootTableProvider::new);
        pack.addProvider((DataProvider.Factory<DataProvider>) NyakoLootTableProvider::new);
        pack.addProvider(NyakoAdvancementProvider::new);
        pack.addProvider(NyakoRecipeProvider::new);
        pack.addProvider(NyakoTagProvider::new);
        // ...
    }
}
