package gay.nyako.nyakomod.screens;

import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.ScreenHandlerSyncHandler;

import java.util.List;

public class CunkShopScreenHandler extends ScreenHandler {
    public final ScreenHandlerContext context;
    public String shopId;

    public CunkShopScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);
        shopId = buf.readString();
    }

    public CunkShopScreenHandler(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
        super(NyakoMod.CUNK_SHOP_SCREEN_HANDLER_TYPE, syncId);
        this.context = context;
        enableSyncing();
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }


}
