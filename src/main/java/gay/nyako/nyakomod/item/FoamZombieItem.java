package gay.nyako.nyakomod.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FoamZombieItem extends Item {
    public FoamZombieItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("Just add water! ™").styled(style -> style.withColor(Formatting.AQUA).withItalic(true)));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
