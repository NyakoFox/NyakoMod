package gay.nyako.nyakomod.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import gay.nyako.nyakomod.NyakoEntities;
import gay.nyako.nyakomod.screens.CunkShopScreenHandler;
import gay.nyako.nyakomod.screens.ShopEntries;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class GenericShopBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {
    private Identifier shopId;

    public GenericShopBlockEntity(BlockPos pos, BlockState state) {
        super(NyakoEntities.GENERIC_SHOP_ENTITY, pos, state);
        shopId = Identifier.of("nyakomod", "main");
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.shopId = Identifier.tryParse(nbt.getString("shop_id"));
        if (this.shopId == null) {
            this.shopId = Identifier.of("nyakomod", "main");
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString("shop_id", this.shopId.toString());
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeIdentifier(shopId);
        JsonObject shopJson = ShopEntries.savedJson.get(shopId);

        if (shopJson == null) {
            shopJson = new JsonObject(); // Fallback to an empty JSON object if not found
            shopJson.addProperty("name", "Invalid Shop");
            shopJson.add("entries", new JsonArray());
        }

        var string = shopJson.toString();
        var length = string.getBytes(StandardCharsets.UTF_8).length;
        buf.writeInt(length);
        buf.writeString(string, length);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Shop");
    }

    @Override
    public @NotNull ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new CunkShopScreenHandler(syncId, inv, ScreenHandlerContext.create(world, player.getBlockPos()));
    }
}