package gay.nyako.nyakomod;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.registry.tag.ItemTags;

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
    }
}
