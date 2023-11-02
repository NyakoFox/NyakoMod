package gay.nyako.nyakomod.screens;

import com.google.common.collect.Lists;
import gay.nyako.nyakomod.Sticker;
import gay.nyako.nyakomod.access.PlayerEntityAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class StickerScreen extends Screen {
    public int mouseX = 0;
    public int mouseY = 0;
    public int scroll = 0;
    private final List<StickerWidget> stickers = Lists.newArrayList();

    public StickerScreen() {
        super(Text.translatable("stickers.title"));
    }

    @Override
    protected void init() {
        stickers.clear();

        MinecraftClient.getInstance().getResourceManager()
                .findAllResources("textures/sticker", id -> id.getPath().endsWith(".png")).forEach(
                        (resourceID, resource) -> {
                            String[] path = resourceID.getPath().split("/");
                            if (path.length == 3)
                            {
                                addSticker(path[2].substring(0, path[2].length() - 4));
                            }
                            else if (path.length == 4)
                            {
                                addStickerPack(path[2], path[3].substring(0, path[3].length() - 4));
                            }
                        }
                );

        readjustStickers();

        super.init();
    }

    private void addStickerPack(String pack, String name) {
        if (((PlayerEntityAccess)MinecraftClient.getInstance().player).getStickerPackCollection().hasStickerPack(pack))
        {
            addSticker(pack + "/" + name);
        }
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
    }

    private void readjustStickers() {
        int stickersPerRow = Math.min((this.width - 32) / (64 + 16), 6);
        int totalHeight = (int) ((Math.ceil((float)stickers.size() / (float)stickersPerRow)) * (64 + 16));
        int scrollMax = totalHeight - this.height + 16;

        if (scroll < -scrollMax)
        {
            scroll = -scrollMax;
        }

        if (scroll > 0)
        {
            scroll = 0;
        }

        // center if the entire thing fits in the screen
        if (scrollMax < 0)
        {
            scroll = (this.height - 16 - totalHeight) / 2;
        }

        for (int index = 0; index < stickers.size(); index++)
        {
            int x = index % stickersPerRow;
            int y = index / stickersPerRow;

            StickerWidget sticker = stickers.get(index);

            sticker.active = true;
            sticker.setX(((this.width - (64 + 16) * stickersPerRow) / 2) + (x * (64 + 16)));
            sticker.setY(16 + (y * (64 + 16)) + scroll);
        }
    }

    public void addSticker(String name) {
        StickerWidget button = new StickerWidget(0, 0, name);
        this.addDrawableChild(button);
        this.stickers.add(button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);

        super.render(context, mouseX, mouseY, delta);

        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    public void renderBackground(DrawContext context) {
        context.fill(0, 0, this.width, this.height, 0x88000000);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        scroll += (int) (amount * 16);

        readjustStickers();

        return true;
    }
}
