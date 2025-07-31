package gay.nyako.nyakomod.compat;

import gay.nyako.nyakomod.recipe.FletchingRecipe;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.screens.FletchingTableScreenHandler;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.block.Blocks;

public class NyakoModREIClientPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<FletchingREIDisplay> FLETCHING = CategoryIdentifier.of(NyakoMod.id("fletching"));

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new FletchingREICategory());
        registry.addWorkstations(FLETCHING, EntryIngredients.of(Blocks.FLETCHING_TABLE));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(FletchingRecipe.class, FletchingREIDisplay::new);
    }

    @Override
    public void registerTransferHandlers(TransferHandlerRegistry registry) {
        registry.register(SimpleTransferHandler.create(
                FletchingTableScreenHandler.class,
                FLETCHING,
                new SimpleTransferHandler.IntRange(0, 3)
        ));
    }
}
