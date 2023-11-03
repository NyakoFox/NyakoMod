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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class StickerPackItem extends Item {

    public StickerPackItem(Settings settings) {
        super(settings);
    }

    public static ItemStack of(String pack)
    {
        ItemStack itemStack = new ItemStack(NyakoItems.STICKER_PACK);
        itemStack.getOrCreateNbt().putString("Pack", pack);
        return itemStack;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient()) {
            user.playSound(NyakoSoundEvents.COIN_COLLECT, SoundCategory.MASTER, 0.2f, ((user.getRandom().nextFloat() - user.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);
            user.playSound(NyakoSoundEvents.STICKER, SoundCategory.MASTER, 0.2f, ((user.getRandom().nextFloat() - user.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);
        }
        user.setStackInHand(hand, ItemStack.EMPTY);

        if (itemStack.getNbt() == null) return TypedActionResult.fail(itemStack);
        if (!itemStack.getNbt().contains("Pack")) return TypedActionResult.fail(itemStack);

        String pack = itemStack.getNbt().getString("Pack");
        if (!((PlayerEntityAccess) user).getStickerPackCollection().hasStickerPack(pack))
        {
            StickerPackCollection collection = ((PlayerEntityAccess) user).getStickerPackCollection();
            collection.addStickerPack(pack);
            ((PlayerEntityAccess) user).setStickerPackCollection(collection);
            if (!world.isClient())
            {
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                var text = Text.translatable("item.nyakomod.sticker_pack.open", Text.translatable("stickers.nyakomod.pack." + pack + ".title").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE).withBold(true)));
                ChatUtils.send((ServerPlayerEntity) user, text, ChatPrefixes.SUCCESS);
            }
            return TypedActionResult.success(itemStack, true);
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        String pack = itemStack.getNbt().getString("Pack");

        tooltip.add(Text.translatable("item.nyakomod.sticker_pack.pack_tooltip",
                Text.translatable("stickers.nyakomod.pack." + pack + ".title")
                        .setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE).withBold(true)))
                .setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        tooltip.add(Text.translatable("item.nyakomod.sticker_pack.open_tooltip")
                .setStyle(Style.EMPTY.withColor(Formatting.GRAY).withItalic(true)));
    }
}
