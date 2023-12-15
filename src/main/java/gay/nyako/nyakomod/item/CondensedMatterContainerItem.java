package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.ChatPrefixes;
import gay.nyako.nyakomod.NyakoItems;
import gay.nyako.nyakomod.NyakoSoundEvents;
import gay.nyako.nyakomod.StickerPackCollection;
import gay.nyako.nyakomod.access.PlayerEntityAccess;
import gay.nyako.nyakomod.utils.ChatUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class CondensedMatterContainerItem extends Item {

    public CondensedMatterContainerItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient()) {
            user.playSound(SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.MASTER, 0.2f, ((user.getRandom().nextFloat() - user.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);
        }

        Optional<RegistryEntry.Reference<Item>> randomItem = Registries.ITEM.getRandom(world.random);
        if (randomItem.isEmpty()) return TypedActionResult.fail(itemStack);

        ItemStack randomItemStack = new ItemStack(randomItem.get());

        user.setStackInHand(hand, randomItemStack);

        if (!world.isClient())
        {
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            var text = Text.translatable("item.nyakomod.condensed_matter_container.open", ((MutableText) randomItemStack.getName()).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE).withBold(true)));
            ChatUtils.send((ServerPlayerEntity) user, text, ChatPrefixes.SUCCESS);
        }
        return TypedActionResult.success(itemStack, true);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable("item.nyakomod.condensed_matter_container.open_tooltip")
                .setStyle(Style.EMPTY.withColor(Formatting.GRAY).withItalic(true)));
    }
}
