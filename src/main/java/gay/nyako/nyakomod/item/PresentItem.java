package gay.nyako.nyakomod.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class PresentItem extends Item {

    public PresentItem(Settings settings) {
        super(settings);
    }

    public static void addToPresent(ItemStack bundle, ItemStack stack) {
        if (stack.isEmpty() || !stack.getItem().canBeNested()) {
            return;
        }

        NbtCompound nbtCompound = bundle.getOrCreateNbt();
        if (!nbtCompound.contains("Items")) {
            nbtCompound.put("Items", new NbtList());
        }

        NbtList nbtList = nbtCompound.getList("Items", 10);

        ItemStack nbtCompound2 = stack.copy();
        NbtCompound itemStack = new NbtCompound();
        nbtCompound2.writeNbt(itemStack);
        nbtList.add(0, itemStack);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (world.isClient()) return TypedActionResult.pass(itemStack);
        user.playSound(SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.MASTER, 0.2f, ((user.getRandom().nextFloat() - user.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);

        user.setStackInHand(hand, ItemStack.EMPTY);

        if (PresentItem.dropAllItems(itemStack, user)) {
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            return TypedActionResult.success(itemStack, true);
        }
        return TypedActionResult.fail(itemStack);
    }

    private static boolean dropAllItems(ItemStack stack, PlayerEntity player) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        if (!nbtCompound.contains("Items")) {
            return false;
        }
        if (player instanceof ServerPlayerEntity) {
            NbtList nbtList = nbtCompound.getList("Items", 10);
            for (int i = 0; i < nbtList.size(); ++i) {
                NbtCompound nbtCompound2 = nbtList.getCompound(i);
                ItemStack itemStack = ItemStack.fromNbt(nbtCompound2);
                if (!player.giveItemStack(itemStack)) {
                    player.dropItem(itemStack, true);
                }
            }
        }
        stack.removeSubNbt("Items");
        return true;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.literal("§7§oRight-click to open this present!"));
    }
}
