package gay.nyako.nyakomod.recipe;

import gay.nyako.nyakomod.NyakoRecipeSerializers;
import gay.nyako.nyakomod.NyakoRecipeTypes;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class FletchingRecipe implements Recipe<CraftingInventory> {
    protected final Identifier id;
    protected final String group;
    protected final ItemStack output;
    protected final Ingredient head;
    protected final Ingredient shaft;
    protected final Ingredient fletching;

    public FletchingRecipe(Identifier id, String group, ItemStack output, Ingredient head, Ingredient shaft, Ingredient fletching) {
        this.id = id;
        this.group = group;
        this.output = output;

        this.head = head;
        this.shaft = shaft;
        this.fletching = fletching;
    }

    public Ingredient getHead() {
        return head;
    }

    public Ingredient getShaft() {
        return shaft;
    }

    public Ingredient getFletching() {
        return fletching;
    }

    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        return getHead().test(inventory.getStack(0)) &&
               getShaft().test(inventory.getStack(1)) &&
               getFletching().test(inventory.getStack(2));
    }

    @Override
    public ItemStack craft(CraftingInventory inventory, DynamicRegistryManager registryManager) {
        return getOutput(registryManager).copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return width == 1 && height == 3;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return output;
    }

    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return NyakoRecipeSerializers.FLETCHING;
    }

    @Override
    public RecipeType<?> getType() {
        return NyakoRecipeTypes.FLETCHING;
    }

    public static class Type implements RecipeType<FletchingRecipe> {
    }
}
