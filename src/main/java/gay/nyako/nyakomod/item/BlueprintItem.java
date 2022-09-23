package gay.nyako.nyakomod.item;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlueprintItem extends Item {

    public BlueprintItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        NbtCompound tag = itemStack.getOrCreateNbt();
        if (tag.contains("blueprint")) {
            var blueprint = tag.getCompound("blueprint");
            if (blueprint.contains("tag")) {
                var blueprintTag = blueprint.getCompound("tag");
                if (blueprintTag.contains("display")) {
                    var display = blueprintTag.getCompound("display");
                    if (display.contains("Name")) {
                        tooltip.add(Text.of("- Contains a name"));
                    }
                    if (display.contains("Lore")) {
                        tooltip.add(Text.of("- Contains lore"));
                    }
                }
                if (blueprintTag.contains("modelId")) {
                    tooltip.add(Text.of("- Contains a custom model"));
                }
            }
        }
    }
}