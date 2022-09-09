package gay.nyako.nyakomod;

import io.wispforest.owo.client.screens.ScreenUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class IconScreenHandler extends ScreenHandler {

    private final ScreenHandlerContext context;
    //private final Property selectedRecipe = Property.create();
    private World world;
    private ItemStack inputStack = ItemStack.EMPTY;
    long lastTakeTime;
    Slot inputSlot;
    Slot copySlot;
    Runnable contentsChangedListener = () -> {};
    public final Inventory inventory = new SimpleInventory(2) {
        @Override
        public void markDirty() {
            super.markDirty();
            updateToClient();
            onContentChanged(this);
            contentsChangedListener.run();
        }
    };

    protected IconScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, ScreenHandlerContext context) {
        super(type, syncId);
        this.context = context;
    }

    public IconScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);
    }

    public IconScreenHandler(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
        super(NyakoMod.ICON_SCREEN_HANDLER_TYPE, syncId);
        int i;
        this.context = context;
        this.world = inventory.player.world;
        this.inputSlot = this.addSlot(new Slot(this.inventory, 0, 143 - 2 - 32 + 2, 33 + 2) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return true;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                context.run((world, pos) -> {
                    long l = world.getTime();
                    if (lastTakeTime != l) {
                        world.playSound(null, (BlockPos)pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        lastTakeTime = l;
                    }
                });
                super.onTakeItem(player, stack);
            }
        });

        this.copySlot = this.addSlot(new Slot(this.inventory, 1, 143 - 2 + 3, 33 + 2) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return true;
            }

            @Override
            public void setStack(ItemStack stack) {
                var stack2 = inventory.getStack(0);

                var nbt = stack.getOrCreateNbt();
                var nbt2 = stack2.getOrCreateNbt();

                if (stack.isEmpty()) {
                    nbt2.remove("modelId");
                } else {
                    String model = Registry.ITEM.getId(stack.getItem()).toString();
                    if (nbt.contains("modelId")) {
                        model = nbt.getString("modelId");
                    }

                    nbt2.putString("modelId", model);
                }

                stack2.setNbt(nbt2);
                super.setStack(stack);
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                context.run((world, pos) -> {
                    var stack2 = inventory.getStack(0);
                    var nbt = stack2.getNbt();
                    if (nbt.contains("modelId")) {
                        nbt.remove("modelId");
                    }
                    stack2.setNbt(nbt);
                });
                super.onTakeItem(player, stack);
            }
        });
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    //public int getSelectedRecipe() {
    //    return this.selectedRecipe.get();
    //}

    public boolean canCraft() {
        return inputSlot.hasStack();
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (this.isInBounds(id)) {
            String model = NyakoMod.customIconURLs.get(id);
            var stack = inputSlot.getStack();
            var nbt = stack.getOrCreateNbt();
            nbt.putString("modelId", model);
            stack.setNbt(nbt);
        }
        return true;
    }

    private boolean isInBounds(int id) {
        return id >= 0 && id < NyakoMod.customIconURLs.size();
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        /*ItemStack itemStack = this.inputSlot.getStack();
        if (!itemStack.isOf(this.inputStack.getItem())) {
            this.inputStack = itemStack.copy();
            this.updateInput(inventory, itemStack);
        }*/
    }

    private void updateInput(Inventory input, ItemStack stack) {
        //this.selectedRecipe.set(-1);
        //this.outputSlot.setStack(ItemStack.EMPTY);
        //if (!stack.isEmpty()) {
        //    this.availableRecipes = this.world.getRecipeManager().getAllMatches(RecipeType.STONECUTTING, input, this.world);
        //}
    }

    void populateResult() {
        //if (isInBounds(this.selectedRecipe.get())) {
            //StonecuttingRecipe stonecuttingRecipe = this.availableRecipes.get(this.selectedRecipe.get());
            //this.output.setLastRecipe(stonecuttingRecipe);
            //this.outputSlot.setStack(stonecuttingRecipe.craft(this.input));
        //} else {
            //this.outputSlot.setStack(ItemStack.EMPTY);
        //}
        this.sendContentUpdates();
    }

    @Override
    public ScreenHandlerType<?> getType() {
        return NyakoMod.ICON_SCREEN_HANDLER_TYPE;
    }

    public void setContentsChangedListener(Runnable contentsChangedListener) {
        this.contentsChangedListener = contentsChangedListener;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        //return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
        return super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return ScreenUtils.handleSlotTransfer(this, index, 1);
        /*
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            Item item = itemStack2.getItem();
            itemStack = itemStack2.copy();
            if (index == 1) {
                item.onCraft(itemStack2, player.world, player);
                if (!this.insertItem(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            } else if (index == 0 ? !this.insertItem(itemStack2, 2, 38, false) : (this.world.getRecipeManager().getFirstMatch(RecipeType.STONECUTTING, new SimpleInventory(itemStack2), this.world).isPresent() ? !this.insertItem(itemStack2, 0, 1, false) : (index >= 2 && index < 29 ? !this.insertItem(itemStack2, 29, 38, false) : index >= 29 && index < 38 && !this.insertItem(itemStack2, 2, 29, false)))) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            }
            slot.markDirty();
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStack2);
            this.sendContentUpdates();
        }
        return itemStack;*/
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.context.run((world, pos) -> {
            this.dropInventory(player, inventory);
        });
    }
}
