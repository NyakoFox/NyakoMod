package gay.nyako.nyakomod.screens;

import gay.nyako.nyakomod.NyakoItems;
import gay.nyako.nyakomod.NyakoScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class BlueprintWorkbenchScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final CraftingResultInventory output = new CraftingResultInventory();
    long lastTakeResultTime;

    public BlueprintWorkbenchScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(3));
    }

    //This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    //and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public BlueprintWorkbenchScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(NyakoScreenHandlers.BLUEPRINT_WORKBENCH_SCREEN_HANDLER_TYPE, syncId);
        checkSize(inventory, 3);
        this.inventory = inventory;
        //some inventories do custom logic when a player opens it.
        inventory.onOpen(playerInventory.player);

        //This will place the slot in the correct locations for a 3x3 Grid. The slots exist on both server and client!
        //This will not render the background of the slots however, this is the Screens job
        int m;
        int l;
        //Our inventory
        var inputSlot = new Slot(this.inventory, 0, 27, 47) {
            @Override
            public void setStackNoCallbacks(ItemStack stack) {
                super.setStackNoCallbacks(stack);
                BlueprintWorkbenchScreenHandler.this.onSlotChanged();
            }
        };

        var blueprintSlot = new Slot(this.inventory, 1, 76, 47) {
            @Override
            public void setStackNoCallbacks(ItemStack stack) {
                super.setStackNoCallbacks(stack);
                BlueprintWorkbenchScreenHandler.this.onSlotChanged();
            }
        };

        this.addSlot(inputSlot);
        this.addSlot(blueprintSlot);
        this.addSlot(new Slot(this.output, 2, 134, 47) {
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            public boolean canTakeItems(PlayerEntity playerEntity) {
//                return ForgingScreenHandler.this.canTakeOutput(playerEntity, this.hasStack());
                return this.hasStack();
            }

            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                onSlotChanged();
                BlueprintWorkbenchScreenHandler.this.onTakeOutput(player, stack);
                long l = player.world.getTime();
                if (lastTakeResultTime != l) {
                    player.world.playSound(null, player.getBlockPos(), SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    lastTakeResultTime = l;
                }
                super.onTakeItem(player, stack);
                onSlotChanged();
            }
        });

        //The player inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        //The player Hotbar
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }

        onSlotChanged();
    }

    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        stack.onCraft(player.world, player, stack.getCount());
        this.decrementStack(1);
    }

    private void decrementStack(int slot) {
        ItemStack itemStack = this.inventory.getStack(slot);
        itemStack.decrement(1);
        this.inventory.setStack(slot, itemStack);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
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
                slot.setStackNoCallbacks(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            slot.onTakeItem(player, originalStack);
            sendContentUpdates();
        }

        return newStack;
    }

    public void onSlotChanged() {
        var input = inventory.getStack(0);
        var blueprint = inventory.getStack(1);

        if (input.isEmpty() || blueprint.isEmpty()) {
            output.setStack(0, new ItemStack(Items.AIR));
            return;
        }

        var newStack = new ItemStack(NyakoItems.BLUEPRINT);
        newStack.setCustomName(Text.translatable("item.nyakomod.blueprint.filled", input.getName()));

        newStack.setCount(1);
        var nbt = newStack.getOrCreateNbt();
        var contents = new NbtCompound();
        nbt.put("blueprint", contents);
        input.writeNbt(contents);
        newStack.setNbt(nbt);

        output.setStack(0, newStack);
    }
}
