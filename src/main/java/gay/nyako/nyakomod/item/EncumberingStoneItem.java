package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.NyakoSoundEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EncumberingStoneItem extends Item {
    public final boolean unlockable;

    public EncumberingStoneItem(Settings settings, boolean unlockable) {
        super(settings);

        this.unlockable = unlockable;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (!unlockable) {
            return false;
        }

        if (clickType == ClickType.LEFT) {
            return false;
        }

        if (otherStack.isEmpty()) {
            var nbt = stack.getOrCreateNbt();

            if (nbt.contains("locked")) {
                nbt.putBoolean("locked", !nbt.getBoolean("locked"));
            } else {
                nbt.putBoolean("locked", false);
            }

            if (nbt.getBoolean("locked")) {
                player.playSound(NyakoSoundEvents.UNLOCK, SoundCategory.BLOCKS, 1f, 0.9f);
            } else {
                player.playSound(NyakoSoundEvents.UNLOCK, SoundCategory.BLOCKS, 1f, 1.1f);
            }

            return true;
        }

        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        var nbt = stack.getOrCreateNbt();

        if (unlockable) {
            tooltip.add(Text.literal("Prevents item pickups while locked").formatted(Formatting.GRAY));
            if (!nbt.contains("locked") || nbt.getBoolean("locked")) {
                tooltip.add(Text.literal("Right Click to unlock").formatted(Formatting.GRAY));
            } else {
                tooltip.add(Text.literal("Right Click to lock").formatted(Formatting.GRAY));
            }
        } else {
            tooltip.add(Text.literal("Prevents item pickups").formatted(Formatting.GRAY));
        }
        tooltip.add(Text.literal("'You are over-encumbered'").formatted(Formatting.GRAY));
    }

    @Override
    public Text getName(ItemStack stack) {
        if (unlockable) {
            var nbt = stack.getNbt();
            if (nbt != null && nbt.contains("locked") && !nbt.getBoolean("locked")) {
                return Text.translatable("item.nyakomod.encumbering_stone.unlocked");
            }
        }
        return Text.translatable(this.getTranslationKey());
    }
}
