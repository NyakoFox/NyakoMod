package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.DoubleSizedSlot;
import gay.nyako.nyakomod.NyakoItems;
import gay.nyako.nyakomod.access.SlotAccess;
import gay.nyako.nyakomod.access.SmithingScreenHandlerAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingScreenHandlerMixin extends ForgingScreenHandler implements SmithingScreenHandlerAccess {
    public SmithingScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    public final Inventory hammerInventory = new SimpleInventory(1) {
        @Override
        public void markDirty() {
            super.markDirty();
        }
    };

    public Slot hammerSlot;

    public Slot getHammerSlot() {
        return hammerSlot;
    }

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At("TAIL"))
    private void injected(CallbackInfo ci) {
        Slot slot = new Slot(hammerInventory, 0, 15, 7);
        ((SlotAccess) slot).setScale(2);
        slot.setStack(new ItemStack(NyakoItems.SMITHING_HAMMER));
        hammerSlot = addSlot(slot);
    }
}
