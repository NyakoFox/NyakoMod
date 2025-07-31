package gay.nyako.nyakomod.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.dynamic.Codecs;

public class FletchingSerializer implements RecipeSerializer<FletchingRecipe> {
    public static final Codec<FletchingRecipe> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codecs.createStrictOptionalFieldCodec(Codec.STRING, "group", "").forGetter(recipe -> recipe.group),
                            ItemStack.RECIPE_RESULT_CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
                            Ingredient.ALLOW_EMPTY_CODEC.fieldOf("head").forGetter(recipe -> recipe.head),
                            Ingredient.ALLOW_EMPTY_CODEC.fieldOf("shaft").forGetter(recipe -> recipe.shaft),
                            Ingredient.ALLOW_EMPTY_CODEC.fieldOf("fletching").forGetter(recipe -> recipe.fletching)
                    )
                    .apply(instance, FletchingRecipe::new)
    );

    @Override
    public Codec<FletchingRecipe> codec() {
        return CODEC;
    }

    @Override
    public FletchingRecipe read(PacketByteBuf buf) {
        ItemStack output = buf.readItemStack();
        String group = buf.readString();
        Ingredient head = Ingredient.fromPacket(buf);
        Ingredient shaft = Ingredient.fromPacket(buf);
        Ingredient fletching = Ingredient.fromPacket(buf);
        return new FletchingRecipe(group, output, head, shaft, fletching);
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
