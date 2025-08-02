package gay.nyako.nyakomod.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class FletchingSerializer implements RecipeSerializer<FletchingRecipe> {
    @Override
    public FletchingRecipe read(Identifier id, JsonObject json) {
        String group = JsonHelper.getString(json, "group", "");
        Ingredient head = Ingredient.fromJson(json.get("head"));
        Ingredient shaft = Ingredient.fromJson(json.get("shaft"));
        Ingredient fletching = Ingredient.fromJson(json.get("fletching"));

        ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));

        return new FletchingRecipe(id, group, output, head, shaft, fletching);
    }

    @Override
    public FletchingRecipe read(Identifier id, PacketByteBuf buf) {
        ItemStack output = buf.readItemStack();
        String group = buf.readString();
        Ingredient head = Ingredient.fromPacket(buf);
        Ingredient shaft = Ingredient.fromPacket(buf);
        Ingredient fletching = Ingredient.fromPacket(buf);
        return new FletchingRecipe(id, group, output, head, shaft, fletching);
    }

    @Override
    public void write(PacketByteBuf buf, FletchingRecipe recipe) {
        buf.writeItemStack(recipe.output);
        buf.writeString(recipe.group);
        recipe.head.write(buf);
        recipe.shaft.write(buf);
        recipe.fletching.write(buf);
    }
}
