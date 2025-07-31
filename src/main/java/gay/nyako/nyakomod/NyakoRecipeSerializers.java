package gay.nyako.nyakomod;

import gay.nyako.nyakomod.recipe.FletchingRecipe;
import gay.nyako.nyakomod.recipe.FletchingSerializer;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class NyakoRecipeSerializers {
    public static final RecipeSerializer<FletchingRecipe> FLETCHING = register("fletching", new FletchingSerializer());

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S variant) {
        return Registry.register(Registries.RECIPE_SERIALIZER, NyakoMod.id(id), variant);
    }

    public static void register() {
    }
}
