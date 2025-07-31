package gay.nyako.nyakomod.screens;

import gay.nyako.nyakomod.*;
import gay.nyako.nyakomod.recipe.FletchingRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.collection.DefaultedList;

import java.util.Optional;

public class FletchingTableScreenHandler extends ScreenHandler {
    public static final int HEAD_SLOT_INDEX = 0;
    public static final int SHAFT_SLOT_INDEX = 1;
    public static final int FLETCHING_SLOT_INDEX = 2;
    public static final int RESULT_SLOT_INDEX = 3;
    private final ScreenHandlerContext context;
    private long lastTakeResultTime;

    public final CraftingInventory inventory = new CraftingInventory(this, 1, 3);
    private final CraftingResultInventory resultInventory = new CraftingResultInventory();

    public FletchingTableScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);
    }

    public FletchingTableScreenHandler(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context) {
        super(NyakoScreenHandlers.FLETCHING_TABLE, syncId);
        this.context = context;

        this.addSlot(new Slot(inventory, HEAD_SLOT_INDEX, 49, 17));
        this.addSlot(new Slot(inventory, SHAFT_SLOT_INDEX, 49, 35));
        this.addSlot(new Slot(inventory, FLETCHING_SLOT_INDEX, 49, 53));

        this.addSlot(new Slot(resultInventory, RESULT_SLOT_INDEX, 107, 35) {
            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                DefaultedList<ItemStack> stacks = player.getWorld().getRecipeManager().getRemainingStacks(NyakoRecipeTypes.FLETCHING, FletchingTableScreenHandler.this.inventory, player.getWorld());
                for (int i = 0; i < stacks.size(); i++) {
                    ItemStack inventoryStack = FletchingTableScreenHandler.this.inventory.getStack(i);
                    ItemStack remainingStack = stacks.get(i);

                    if (!inventoryStack.isEmpty())
                    {
                        FletchingTableScreenHandler.this.inventory.removeStack(i, 1);
                        inventoryStack = FletchingTableScreenHandler.this.inventory.getStack(i);
                    }

                    if (!remainingStack.isEmpty()) {
                        if (inventoryStack.isEmpty()) {
                            FletchingTableScreenHandler.this.inventory.setStack(i, remainingStack);
                        } else if (ItemStack.canCombine(inventoryStack, remainingStack)) {
                            inventoryStack.increment(remainingStack.getCount());
                            FletchingTableScreenHandler.this.inventory.setStack(i, inventoryStack);
                        } else if (!player.getInventory().insertStack(remainingStack)) {
                            player.dropItem(remainingStack, false);
                        }
                    }
                }

                stack.getItem().onCraft(stack, player.getWorld(), player);
                context.run((world, pos) -> {
                long l = world.getTime();
                if (lastTakeResultTime != l) {
                    world.playSound(null, pos, NyakoSoundEvents.UI_FLETCHING_TABLE_TAKE_RESULT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    lastTakeResultTime = l;
                    }
                });
            }
        });

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }


    @Override
    public void onContentChanged(Inventory inventory) {
        this.context.run((world, pos) -> {
            if (world.isClient()) {
                return;
            }

            Optional<FletchingRecipe> match = world.getRecipeManager().getFirstMatch(NyakoRecipeTypes.FLETCHING, this.inventory, world);

            ItemStack outputStack = ItemStack.EMPTY;

            if (match.isPresent())
            {
                FletchingRecipe recipe = match.get();
                outputStack = recipe.craft(this.inventory, world.getRegistryManager());
            }

            resultInventory.setStack(RESULT_SLOT_INDEX, outputStack);
            sendContentUpdates();
        });
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        Slot slot = slots.get(slotIndex);

        if (slot.hasStack()) {
            ItemStack slotStack = slot.getStack();
            ItemStack resultStack = slotStack.copy();

            if (slotIndex == RESULT_SLOT_INDEX) {
                // Shift clicking result slot
                slotStack.getItem().onCraft(slotStack, player.getWorld(), player);
                if (!insertItem(slotStack, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(slotStack, resultStack);
            } else if (slotIndex >= 4 && slotIndex < 40) {
                if (!insertItem(slotStack, 0, 3, false)) {
                    if (slotIndex < 31) {
                        if (!insertItem(slotStack, 31, 40, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (insertItem(slotStack, 4, 31, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.insertItem(slotStack, 4, 40, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (slotStack.getCount() == resultStack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, slotStack);
            if (slotIndex == RESULT_SLOT_INDEX) {
                player.dropItem(slotStack, false);
            }

            return resultStack;
        }

        return ItemStack.EMPTY;
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
