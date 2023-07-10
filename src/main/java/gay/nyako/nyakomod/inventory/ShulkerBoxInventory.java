package gay.nyako.nyakomod.inventory;

import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;

public class ShulkerBoxInventory extends SimpleInventory {
    public final ItemStack itemStack;
    protected static final int SIZE = 27;

    static final String NBT_TAG = "BlockEntityTag";
    static final String NBT_ITEMS = "Items";

    public ShulkerBoxInventory(ItemStack stack) {
        super(getContents(stack));
        itemStack = stack;
    }

    static boolean hasItemsNbt(NbtCompound nbt) {
        return nbt.contains(NBT_ITEMS, NbtElement.LIST_TYPE);
    }

    static DefaultedList<ItemStack> defaultContents() {
        return DefaultedList.ofSize(SIZE, ItemStack.EMPTY);
    }

    public static ItemStack[] getContents(ItemStack stack) {
        var nbt = stack.getSubNbt(NBT_TAG);

        var contents = defaultContents();

        if (nbt != null && hasItemsNbt(nbt)) {
            Inventories.readNbt(nbt, contents);
        }

        return contents.toArray(new ItemStack[SIZE]);
    }

    @Override
    public void markDirty() {
        var nbt = itemStack.getOrCreateNbt();

        if (!isEmpty()) {
            var contents = defaultContents();
            for (int i = 0; i < size(); i++) {
                contents.set(i, getStack(i));
            }
            var subNbt = nbt.getCompound(NBT_TAG);
            Inventories.writeNbt(subNbt, contents);
            nbt.put(NBT_TAG, subNbt);
        } else if (hasItemsNbt(nbt)) {
            nbt.remove(NBT_TAG);
        }

        NyakoMod.LOGGER.info(nbt.contains(NBT_TAG));

        itemStack.setNbt(nbt);

        super.markDirty();
    }

    @Override
    public void onClose(PlayerEntity player) {
        var pos = player.getPos();
        var world = player.world;

        world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_SHULKER_BOX_CLOSE, SoundCategory.BLOCKS, 0.5f, world.random.nextFloat() * 0.1f + 0.9f);

        markDirty();
    }
}
