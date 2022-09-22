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

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CunkShopScreenHandler extends ScreenHandler {
    public final ScreenHandlerContext context;
    public Identifier shopId;

    public CunkShopScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);
        shopId = buf.readIdentifier();
        var length = buf.readInt();
        var json = buf.readString(length);

        var shopData = new ShopData(shopId);

        // Use GSon to parse the JSON file into a JsonObject
        JsonObject shopJson = JsonParser.parseString(json).getAsJsonObject();
        NyakoMod.loadShopModelFromJson(shopJson, shopData);
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
