package gay.nyako.nyakomod.screens;

import gay.nyako.nyakomod.NyakoItems;
import gay.nyako.nyakomod.NyakoScreenHandlers;
import gay.nyako.nyakomod.item.PresentItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class PresentWrapperScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final CraftingResultInventory output = new CraftingResultInventory();
    long lastTakeResultTime;

    public PresentWrapperScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(6));
    }

    //This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    //and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public PresentWrapperScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(NyakoScreenHandlers.PRESENT_WRAPPER_SCREEN_HANDLER_TYPE, syncId);
        checkSize(inventory, 6);
        this.inventory = inventory;
        //some inventories do custom logic when a player opens it.
        inventory.onOpen(playerInventory.player);


        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(this.inventory, j + i * 3, 30 + j * 18, 17 + i * 18 + 9) {
                    @Override
                    public void setStack(ItemStack stack) {
                        super.setStack(stack);
                        PresentWrapperScreenHandler.this.onSlotChanged();
                    }
                });
            }
        }

        this.addSlot(new Slot(this.output, 0, 124, 35) {
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            public boolean canTakeItems(PlayerEntity playerEntity) {
                return this.hasStack();
            }

            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                onSlotChanged();
                PresentWrapperScreenHandler.this.onTakeOutput(player, stack);
                long l = player.world.getTime();
                if (lastTakeResultTime != l) {
                    player.world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_BUNDLE_INSERT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    lastTakeResultTime = l;
                }
                super.onTakeItem(player, stack);
                onSlotChanged();
            }
        });

        //The player inventory
        for (int m = 0; m < 3; ++m) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        //The player Hotbar
        for (int m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }

        onSlotChanged();
    }

    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        stack.onCraft(player.world, player, stack.getCount());
        for (int i = 0; i < 6; ++i) {
            this.inventory.setStack(i, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            slot.onTakeItem(player, originalStack);
            sendContentUpdates();
        }

        return newStack;
    }

    public void onSlotChanged() {
        var filled = false;
        for (int i = 0; i < 6; i++) {
            if (!inventory.getStack(i).isEmpty()) {
                filled = true;
                break;
            }
        }
        if (!filled) {
            output.setStack(0, ItemStack.EMPTY);
            return;
        }

        var newStack = new ItemStack(NyakoItems.PRESENT);

        for (int i = 0; i < 6; i++) {
            var stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                PresentItem.addToPresent(newStack, stack.copy());
            }
        }

        newStack.setCustomName(Text.of("Present (Filled)"));
        newStack.setCount(1);

        output.setStack(0, newStack);
    }
}
