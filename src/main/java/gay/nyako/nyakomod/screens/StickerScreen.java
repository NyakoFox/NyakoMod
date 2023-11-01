package gay.nyako.nyakomod.screens;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class StickerScreen extends Screen {
    public int mouseX = 0;
    public int mouseY = 0;
    private final List<StickerWidget> stickers = Lists.newArrayList();

    public StickerScreen() {
        super(Text.translatable("stickers.title"));
    }

    @Override
    protected void init() {
        stickers.clear();
        addSticker("cool_idea");
        addSticker("skill_issue");
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
        super.init();
    }

    public void addSticker(String name) {
        int x = this.stickers.size() % 4;
        int y = this.stickers.size() / 4;

        int offX = (this.width - (64 + 16) * 4) / 2;
        int offY = (this.height - (64 + 16) * 4) / 2;

        StickerWidget button = new StickerWidget(offX + (x * (64 + 16)), offY + (y * (64 + 16)), name);
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
}
