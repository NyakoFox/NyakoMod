package gay.nyako.nyakomod;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StickerSystem {
    public static List<Sticker> STICKERS = new ArrayList<>();

    public static void render(DrawContext drawContext, float tickDelta) {
        int y = drawContext.getScaledWindowHeight() / 2 - (32 * STICKERS.size());

        Iterator<Sticker> iterator = STICKERS.iterator();
        while (iterator.hasNext()) {
            int x = drawContext.getScaledWindowWidth() - 64 - 8;

            Sticker sticker = iterator.next();
            sticker.tickDelta = tickDelta;

            if (sticker.ticks > 100 - 10)
            {
                x = drawContext.getScaledWindowWidth();
            }

            sticker.targetX = x;
            sticker.targetY = y;

            sticker.render(drawContext);

            y += 64;
        }
    }

    public static void addSticker(Text player, String swellow, boolean client) {
        MinecraftClient.getInstance().player.playSound(NyakoSoundEvents.STICKER, SoundCategory.PLAYERS, 1f, 1f);

        Sticker sticker = new Sticker();
        sticker.player = player;
        sticker.client = client;
        sticker.name = swellow;
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
        addSticker(MinecraftClient.getInstance().player.getDisplayName(), name, true);
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeString(name);
        ClientPlayNetworking.send(NyakoNetworking.SEND_STICKER, passedData);
    }
}
