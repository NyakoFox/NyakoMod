package gay.nyako.nyakomod.screens;

import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class CunkShopScreenHandler extends ScreenHandler {
    public final ScreenHandlerContext context;
    public Identifier shopId;

    public CunkShopScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);
        shopId = buf.readIdentifier();
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
