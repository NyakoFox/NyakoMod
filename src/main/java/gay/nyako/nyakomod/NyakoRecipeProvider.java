package gay.nyako.nyakomod;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;

import java.util.function.Consumer;

public class NyakoRecipeProvider extends FabricRecipeProvider {
    public NyakoRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        offerPlanksRecipe(exporter, NyakoBlocks.ECHO_PLANKS, NyakoItemTags.ECHO_SPINES, 4);
        offerHangingSignRecipe(exporter, NyakoBlocks.ECHO_HANGING_SIGN, NyakoBlocks.STRIPPED_ECHO_SPINE);
        offerBarkBlockRecipe(exporter, NyakoBlocks.ECHO_SPUR, NyakoBlocks.ECHO_SPINE);
        offerBarkBlockRecipe(exporter, NyakoBlocks.STRIPPED_ECHO_SPUR, NyakoBlocks.STRIPPED_ECHO_SPINE);
        RecipeProvider.generateFamily(exporter, NyakoBlockFamilies.ECHO);

        offerPlanksRecipe(exporter, NyakoBlocks.BENTHIC_PLANKS, NyakoItemTags.BENTHIC_SPINES, 4);
        offerHangingSignRecipe(exporter, NyakoBlocks.BENTHIC_HANGING_SIGN, NyakoBlocks.STRIPPED_BENTHIC_SPINE);
        offerBarkBlockRecipe(exporter, NyakoBlocks.BENTHIC_SPUR, NyakoBlocks.BENTHIC_SPINE);
        offerBarkBlockRecipe(exporter, NyakoBlocks.STRIPPED_BENTHIC_SPUR, NyakoBlocks.STRIPPED_BENTHIC_SPINE);
        RecipeProvider.generateFamily(exporter, NyakoBlockFamilies.BENTHIC);
    }
}
