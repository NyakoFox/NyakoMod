package gay.nyako.nyakomod.screens;

import gay.nyako.nyakomod.NyakoItems;
import gay.nyako.nyakomod.NyakoScreenHandlers;
import gay.nyako.nyakomod.NyakoSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class FletchingTableScreenHandler extends ScreenHandler {
    public static final int HEAD_SLOT_INDEX = 0;
    public static final int BODY_SLOT_INDEX = 1;
    public static final int TAIL_SLOT_INDEX = 2;
    public static final int RESULT_SLOT_INDEX = 3;
    private final ScreenHandlerContext context;
    long lastTakeResultTime;

    public final Inventory inventory = new SimpleInventory(3) {
        @Override
        public void markDirty() {
            onContentChanged(this);
            super.markDirty();
        }
    };
    private final CraftingResultInventory resultInventory = new CraftingResultInventory(){

        @Override
        public void markDirty() {
            onContentChanged(this);
            super.markDirty();
        }
    };

    public FletchingTableScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);
    }

    public FletchingTableScreenHandler(int syncId, PlayerInventory inventory, final ScreenHandlerContext context) {
        super(NyakoScreenHandlers.FLETCHING_TABLE, syncId);
        this.context = context;
        this.addSlot(new Slot(this.inventory, HEAD_SLOT_INDEX, 49, 17) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.FLINT) || stack.isOf(Items.OBSIDIAN);
            }
        });
        this.addSlot(new Slot(this.inventory, BODY_SLOT_INDEX, 49, 35) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.STICK) || stack.isOf(Items.BAMBOO);
            }
        });
        this.addSlot(new Slot(this.inventory, TAIL_SLOT_INDEX, 49, 53) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.FEATHER) || stack.isOf(Items.PAPER);
            }
        });

        this.addSlot(new Slot(this.resultInventory, RESULT_SLOT_INDEX, 107, 35) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                slots.get(HEAD_SLOT_INDEX).takeStack(1);
                slots.get(BODY_SLOT_INDEX).takeStack(1);
                slots.get(TAIL_SLOT_INDEX).takeStack(1);
                stack.getItem().onCraft(stack, player.getWorld());
                context.run((world, pos) -> {
                    long l = world.getTime();
                    if (lastTakeResultTime != l) {
                        world.playSound(null, pos, NyakoSoundEvents.UI_FLETCHING_TABLE_TAKE_RESULT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        lastTakeResultTime = l;
                    }
                });
                super.onTakeItem(player, stack);
            }
        });
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        ItemStack itemStack = this.inventory.getStack(HEAD_SLOT_INDEX);
        ItemStack itemStack2 = this.inventory.getStack(BODY_SLOT_INDEX);
        ItemStack itemStack3 = this.inventory.getStack(TAIL_SLOT_INDEX);
        ItemStack itemStack4 = this.resultInventory.getStack(RESULT_SLOT_INDEX);
        if (!itemStack4.isEmpty() && (itemStack.isEmpty() || itemStack2.isEmpty() || itemStack3.isEmpty())) {
            this.resultInventory.removeStack(RESULT_SLOT_INDEX);
        } else if (!itemStack.isEmpty() && !itemStack2.isEmpty() && !itemStack3.isEmpty()) {
            this.updateResult(itemStack, itemStack2, itemStack3, itemStack4);
        }
    }

    private void updateResult(ItemStack head, ItemStack body, ItemStack tail, ItemStack oldResult) {
        this.context.run((world, pos) -> {
            ItemStack result = new ItemStack(Items.ARROW);
            if (head.getItem() == Items.OBSIDIAN)
            {
                result = new ItemStack(NyakoItems.OBSIDIAN_ARROW);
            }

            if (!ItemStack.areEqual(result, oldResult)) {
                this.resultInventory.setStack(RESULT_SLOT_INDEX, result);
                this.sendContentUpdates();
            }
        });
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = slots.get(slot);
        if (slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot == 3) {
                itemStack2.getItem().onCraft(itemStack2, player.getWorld());
                if (!this.insertItem(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot2.onQuickTransfer(itemStack2, itemStack);
            } else if (slot == 1 || slot == 0 ? !this.insertItem(itemStack2, 3, 39, false) : (itemStack2.isOf(Items.FILLED_MAP) ? !this.insertItem(itemStack2, 0, 1, false) : (itemStack2.isOf(Items.PAPER) || itemStack2.isOf(Items.MAP) || itemStack2.isOf(Items.GLASS_PANE) ? !this.insertItem(itemStack2, 1, 2, false) : (slot >= 3 && slot < 30 ? !this.insertItem(itemStack2, 30, 39, false) : slot >= 30 && slot < 39 && !this.insertItem(itemStack2, 3, 30, false))))) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            }
            slot2.markDirty();
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot2.onTakeItem(player, itemStack2);
            this.sendContentUpdates();
        }
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.resultInventory.removeStack(3);
        this.context.run((world, pos) -> this.dropInventory(player, this.inventory));
    }
}
