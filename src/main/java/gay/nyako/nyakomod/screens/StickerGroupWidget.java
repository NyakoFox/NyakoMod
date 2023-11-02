package gay.nyako.nyakomod.screens;

import gay.nyako.nyakomod.Sticker;
import gay.nyako.nyakomod.StickerSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class StickerGroupWidget extends PressableWidget {
    public final String name;
    public final List<String> stickerNames = Lists.newArrayList();

    private boolean selected = false;

    public StickerGroupWidget(int x, int y, String name) {
        super(x, y, 96, 16, Text.of(name));
        this.name = name;
    }

    @Override
    public void onPress() {
        selected = true;
        try {
            var screen = (StickerScreen) MinecraftClient.getInstance().currentScreen;
            screen.loadStickerPack(this);
            selected = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        var text = Text.translatable("stickers.nyakomod.pack." + name + ".title");
        if (selected) {
            text = text.setStyle(Style.EMPTY.withUnderline(true));
        }
        context.drawText(textRenderer, text, this.getX() + 4, this.getY() + 4, 0xFFFFFFFF, true);
        // context.drawTexture(texture, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void clearStickers() {
        stickerNames.clear();
    }

    public void addSticker(String name) {
        stickerNames.add(name);
    }

    public void deselect() {
        selected = false;
    }

    public boolean isSelected() {
        return selected;
    }
}
