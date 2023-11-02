package gay.nyako.nyakomod;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class StickerSystem {
    public static List<Sticker> STICKERS = new ArrayList<>();

    public static int STICKER_WIDTH = 64;
    public static int STICKER_HEIGHT = 64;
    public static int STICKER_PADDING = 8;

    public static void render(DrawContext drawContext, float tickDelta) {
        int y = drawContext.getScaledWindowHeight() / 2 - (STICKERS.size() * (STICKER_HEIGHT + STICKER_PADDING)) / 2;

        Iterator<Sticker> iterator = STICKERS.iterator();
        while (iterator.hasNext()) {
            int x = drawContext.getScaledWindowWidth() - STICKER_WIDTH - STICKER_PADDING;

            Sticker sticker = iterator.next();
            sticker.tickDelta = tickDelta;

            if (sticker.ticks > 100 - 10)
            {
                x = drawContext.getScaledWindowWidth();
            }

            sticker.targetX = x;
            sticker.targetY = y;

            sticker.render(drawContext);

            y += STICKER_HEIGHT + STICKER_PADDING;
        }
    }

    public static void addSticker(Text player, String stickerID, UUID playerUUID) {
        MinecraftClient.getInstance().player.playSound(NyakoSoundEvents.STICKER, SoundCategory.PLAYERS, 1f, 1f);

        Sticker sticker = new Sticker();
        sticker.playerName = player;
        sticker.stickerID = stickerID;
        sticker.playerUUID = playerUUID;
        sticker.oldTicks = 0;
        sticker.tickDelta = 0;
        sticker.ticks = 0;
        sticker.currentX = -1;
        sticker.currentY = -1;

        STICKERS.add(sticker);
    }

    public static void tick(MinecraftClient minecraftClient) {
        Iterator<Sticker> iterator = STICKERS.iterator();
        while (iterator.hasNext()) {
            Sticker sticker = iterator.next();
            sticker.tick();
            sticker.oldTicks = sticker.ticks;
            sticker.ticks++;

            if (sticker.ticks > 100) {
                iterator.remove();
            }
        }
    }

    public static void showSticker(String name) {
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeString(name);
        ClientPlayNetworking.send(NyakoNetworking.SEND_STICKER, passedData);
    }
}
