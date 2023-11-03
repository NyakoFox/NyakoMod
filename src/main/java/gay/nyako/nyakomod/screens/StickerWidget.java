package gay.nyako.nyakomod.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import gay.nyako.nyakomod.Sticker;
import gay.nyako.nyakomod.StickerSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.gui.tooltip.WidgetTooltipPositioner;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class StickerWidget extends PressableWidget {
    public final String name;
    public final Identifier texture;
    public StickerWidget(int x, int y, String name) {
        super(x, y, 64, 64, Text.of(name));
        this.name = name;
        texture = new Identifier("nyakomod", "textures/sticker/" + name + ".png");
    }

    @Override
    protected TooltipPositioner getTooltipPositioner() {
        return HoveredTooltipPositioner.INSTANCE;
    }

    @Override
    public void onPress() {
        for (Sticker sticker : StickerSystem.STICKERS)
        {
            if (sticker.playerUUID.equals(MinecraftClient.getInstance().player.getUuid()) && sticker.ticks < 50) {
                return;
            }
        }

        MinecraftClient.getInstance().setScreen(null);
        StickerSystem.showSticker(name);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        if (isHovered())
        {
            context.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
            context.drawBorder(this.getX() - 1, this.getY() - 1, this.width + 2, this.height + 2, 0xFFFFFFFF);
        }
        else
        {
            context.setShaderColor(0.8f, 0.8f, 0.8f, this.alpha);
        }
        context.drawTexture(texture, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
