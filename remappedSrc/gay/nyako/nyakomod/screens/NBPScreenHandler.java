package gay.nyako.nyakomod.screens;

import gay.nyako.nyakomod.NyakoScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.math.BlockPos;

public class NBPScreenHandler extends ScreenHandler {
    public final ScreenHandlerContext context;

    public BlockPos blockPos;

    public NBPScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);

        blockPos = buf.readBlockPos();
    }

    public NBPScreenHandler(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
        super(NyakoScreenHandlers.NBP_SCREEN_HANDLER_TYPE, syncId);
        this.context = context;
        enableSyncing();
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }


}
