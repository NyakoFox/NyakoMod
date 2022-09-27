package gay.nyako.nyakomod.screens;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class NBPScreenHandler extends ScreenHandler {
    public final ScreenHandlerContext context;

    public BlockPos blockPos;

    public NBPScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);

        blockPos = buf.readBlockPos();
    }

    public NBPScreenHandler(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
        super(NyakoMod.NBP_SCREEN_HANDLER_TYPE, syncId);
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
