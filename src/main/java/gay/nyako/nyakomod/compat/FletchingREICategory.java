package gay.nyako.nyakomod.compat;

import gay.nyako.nyakomod.screens.FletchingTableScreen;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class FletchingREICategory implements DisplayCategory<FletchingREIDisplay> {

    @Override
    public List<Widget> setupDisplay(FletchingREIDisplay display, Rectangle bounds) {
        Point origin = bounds.getLocation();

        final ArrayList<Widget> widgets = new ArrayList<>();
        final Identifier texture = FletchingTableScreen.TEXTURE;

        widgets.add(Widgets.createRecipeBase(bounds));

        // The "content" in the texture file!
        int widgetWidth = 80;
        int widgetHeight = 54;

        // Calculate where we should place the "content" of the texture into the widget
        int displayX = origin.getX() + (bounds.width - widgetWidth) / 2;
        int displayY = origin.getY() + (bounds.height - widgetHeight) / 2;

        // Display the texture
        widgets.add(Widgets.createTexturedWidget(texture, displayX, displayY, 48, 16, widgetWidth, widgetHeight));

        // Display the inputs
        List<EntryIngredient> inputEntries = display.getInputEntries();
        for (int i = 0; i < inputEntries.size(); i++) {
            EntryIngredient entry = inputEntries.get(i);
            int x = displayX + 1;
            int y = displayY + 1 + (i * 18);
            widgets.add(Widgets.createSlot(new Point(x, y)).entries(entry).disableBackground().markInput());
        }

        // Display the output
        EntryIngredient output = display.getOutputEntries().get(0);
        widgets.add(Widgets.createSlot(new Point(displayX + 59, displayY + 19)).entries(output).disableBackground().markOutput());

        return widgets;
    }

    @Override
    public CategoryIdentifier<? extends FletchingREIDisplay> getCategoryIdentifier() {
        return NyakoModREIClientPlugin.FLETCHING;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("category.nyakomod.fletching");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.FLETCHING_TABLE);
    }
}
