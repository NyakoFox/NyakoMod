package gay.nyako.nyakomod.screens;

import com.google.common.collect.Lists;
import gay.nyako.nyakomod.Sticker;
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
        addSticker("cool_idea");
        addSticker("skill_issue");
        addSticker("cry_about_it");
        addSticker("slugcat_wave");
        addSticker("swellow");
        addSticker("very_good");
        addSticker("waits_faster");
        addSticker("wrong");
        addSticker("i_forgor");
        addSticker("i_rember");
        addSticker("mindblowing");
        addSticker("so_ive_heard");
        addSticker("does_he_know");
        addSticker("adachi_true");
        addSticker("adachi_false");
        addSticker("adachi_maybe");
        addSticker("adachi_cringe");
        addSticker("out_of_my_way");
        addSticker("im_gonna_cry");
        addSticker("post_this_when_you_winning");

        readjustStickers();

        super.init();
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
    }

    private void readjustStickers() {
        int stickersPerRow = Math.min((this.width - 32) / (64 + 16), 6);
        int totalHeight = (stickers.size() / stickersPerRow) * (64 + 16);
        int scrollMax = totalHeight - (this.height - 32);

        if (scroll < 0)
        {
            scroll = 0;
        }

        if (scroll > scrollMax)
        {
            scroll = scrollMax;
        }

        if (totalHeight < this.height - 32)
        {
            // center
            scroll = (this.height - 32 - totalHeight) / 2;
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
