package gay.nyako.nyakomod.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class DragonScaleItem extends Item {
    public DragonScaleItem(FabricItemSettings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("Wait... isn't this just denim?").formatted(Formatting.LIGHT_PURPLE, Formatting.ITALIC));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
