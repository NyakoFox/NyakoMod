package gay.nyako.nyakomod;

import gay.nyako.nyakomod.recipe.FletchingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class NyakoRecipeTypes {
    public static final RecipeType<FletchingRecipe> FLETCHING = register("fletching", new FletchingRecipe.Type());

    private static <S extends RecipeType<T>, T extends Recipe<?>> S register(String id, S variant) {
        return Registry.register(Registries.RECIPE_TYPE, NyakoMod.id(id), variant);
    }

    public static void register() {
    }
}
