package gay.nyako.nyakomod.mixin;

import eu.pb4.placeholders.api.TextParserUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnderDragonPart.class)
public abstract class EnderDragonPartMixin extends Entity {

    public EnderDragonPartMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public final ActionResult interact(PlayerEntity player, Hand hand) {
        if (!this.isAlive()) {
            return ActionResult.PASS;
        }

        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.BUCKET)) {
            player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0f, 1.0f);
            var milkStack = Items.MILK_BUCKET.getDefaultStack();
            var milkNbt = milkStack.getOrCreateNbt();

            NbtCompound nbtDisplay = milkNbt.getCompound(ItemStack.DISPLAY_KEY);
            NbtList nbtLore = nbtDisplay.getList(ItemStack.LORE_KEY, NbtElement.STRING_TYPE);
            nbtLore.add(NbtString.of(Text.Serializer.toJson(TextParserUtils.formatText("<gradient:blue:green>Milk from the void.</gradient>"))));
            nbtDisplay.put(ItemStack.LORE_KEY, nbtLore);
            // Add name
            nbtDisplay.putString(ItemStack.NAME_KEY, Text.Serializer.toJson(TextParserUtils.formatText("<gradient:light_purple:dark_purple>Dragon Milk</gradient>")));
            milkNbt.put(ItemStack.DISPLAY_KEY, nbtDisplay);

            milkNbt.putBoolean("Authentic", true);

            milkStack.setNbt(milkNbt);

            ItemStack itemStack2 = ItemUsage.exchangeStack(itemStack, player, milkStack);
            player.setStackInHand(hand, itemStack2);

            this.emitGameEvent(GameEvent.ENTITY_INTERACT);
            return ActionResult.success(this.world.isClient);
        }

        return super.interact(player, hand);
    }
}