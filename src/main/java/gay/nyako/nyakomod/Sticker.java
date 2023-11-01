package gay.nyako.nyakomod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class Sticker {
    public PlayerEntity player = null;
    public String name = null;
    public float oldTicks = 0;
    public float ticks = 0;
    public float tickDelta = 0;

    public float currentX = -1;
    public float currentY = -1;

    public void render(DrawContext drawContext, int targetX, int targetY)
    {
        int width = 64;
        int height = 64;

        if (currentX == -1) currentX = drawContext.getScaledWindowWidth();
        if (currentY == -1) currentY = drawContext.getScaledWindowHeight() / 2f - (height/2f);

        currentX = (float) MathHelper.lerp(0.1 * tickDelta, currentX, targetX);
        currentY = (float) MathHelper.lerp(0.1 * tickDelta, currentY, targetY);

        drawContext.setShaderColor(0, 0, 0, 0.5f);
        drawContext.drawTexture(getSticker(name), (int) currentX + 1, (int) currentY + 1, 0, 0, width, height, width, height);
        drawContext.setShaderColor(1, 1, 1, 1);
        drawContext.drawTexture(getSticker(name), (int) currentX, (int) currentY, 0, 0, width, height, width, height);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        Text playerName = player.getDisplayName();
        drawContext.drawText(textRenderer, playerName, (int) (currentX + (width/2f) - textRenderer.getWidth(playerName) / 2) + 1, (int) currentY + 1, 0xFF000000, false);
        drawContext.drawText(textRenderer, playerName, (int) (currentX + (width/2f) - textRenderer.getWidth(playerName) / 2), (int) currentY, 0xFFFFFFFF, false);

    }

    public static Identifier getSticker(String name) {
        return new Identifier("nyakomod", "textures/sticker/" + name + ".png");
    }

}
