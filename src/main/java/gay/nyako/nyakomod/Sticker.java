package gay.nyako.nyakomod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.UUID;

public class  Sticker {
    public Text playerName = null;
    public String stickerID = null;
    public UUID playerUUID = null;
    public float oldTicks = 0;
    public float ticks = 0;
    public float tickDelta = 0;

    public float lastX = -1;
    public float lastY = -1;
    public float currentX = -1;
    public float currentY = -1;
    public float targetX = -1;
    public float targetY = -1;

    public void render(DrawContext drawContext)
    {
        int width = StickerSystem.STICKER_WIDTH;
        int height = StickerSystem.STICKER_HEIGHT;

        if (currentX == -1) {
            currentX = drawContext.getScaledWindowWidth();
            lastX = currentX;
        }
        if (currentY == -1) {
            currentY = drawContext.getScaledWindowHeight() / 2f - (height/2f);
            lastY = currentY;
        }

        float drawX = MathHelper.lerp(tickDelta, lastX, currentX);
        float drawY = MathHelper.lerp(tickDelta, lastY, currentY);

        drawContext.setShaderColor(0, 0, 0, 0.5f);
        drawContext.drawTexture(getSticker(stickerID), (int) drawX + 1, (int) drawY + 1, 0, 0, width, height, width, height);
        drawContext.setShaderColor(1, 1, 1, 1);
        drawContext.drawTexture(getSticker(stickerID), (int) drawX, (int) drawY, 0, 0, width, height, width, height);

        if (playerName != null) {
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            drawContext.drawText(textRenderer, playerName, (int) (drawX + (width / 2f) - textRenderer.getWidth(playerName) / 2), (int) drawY, 0xFFFFFFFF, true);
        }
    }

    public static Identifier getSticker(String name) {
        return new Identifier("nyakomod", "textures/sticker/" + name + ".png");
    }

    public void tick() {
        if (currentX == -1) return;
        if (currentY == -1) return;

        lastX = currentX;
        lastY = currentY;
        currentX = MathHelper.lerp(0.5f * tickDelta, currentX, targetX);
        currentY = MathHelper.lerp(0.5f * tickDelta, currentY, targetY);

    }
}
