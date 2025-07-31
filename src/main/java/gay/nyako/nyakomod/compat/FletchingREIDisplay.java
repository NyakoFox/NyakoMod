package gay.nyako.nyakomod.compat;

import gay.nyako.nyakomod.recipe.FletchingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;

import java.util.List;

public class FletchingREIDisplay implements Display {
    private final FletchingRecipe recipe;

    public FletchingREIDisplay(FletchingRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(
                EntryIngredients.ofIngredient(recipe.getHead()),
                EntryIngredients.ofIngredient(recipe.getShaft()),
                EntryIngredients.ofIngredient(recipe.getFletching())
        );
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of(EntryIngredients.of(recipe.getResult()));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return NyakoModREIClientPlugin.FLETCHING;
    }
}
