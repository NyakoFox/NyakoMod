package gay.nyako.nyakomod.screens;

import com.google.common.collect.Lists;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.Sticker;
import gay.nyako.nyakomod.access.PlayerEntityAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StickerScreen extends Screen {
    public int mouseX = 0;
    public int mouseY = 0;
    public int scrollPacks = 0;
    public int scrollStickers = 0;

    private final int SIDEBAR_WIDTH = 160;

    private List<StickerGroupWidget> orderedStickerPacks = Lists.newArrayList();
    private final HashMap<String, StickerGroupWidget> stickerPacks = new HashMap<>();
    private final List<StickerWidget> stickers = Lists.newArrayList();

    private static final String ALL_PACK_KEY = "all";
    private static final String DEFAULT_PACK_KEY = "default";

    public StickerScreen() {
        super(Text.translatable("stickers.title"));
    }

    @Override
    protected void init() {
        for (var pack : stickerPacks.values()) {
            pack.clearStickers();
            this.remove(pack);
        }
        stickerPacks.clear();

        getStickerGroup(ALL_PACK_KEY);
        getStickerGroup(DEFAULT_PACK_KEY);

        MinecraftClient.getInstance().getResourceManager()
                .findAllResources("textures/sticker", id -> id.getPath().endsWith(".png")).forEach(
                        (resourceID, resource) -> {
                            String[] path = resourceID.getPath().split("/");
                            if (path.length == 3)
                            {
                                addSticker(DEFAULT_PACK_KEY, path[2].substring(0, path[2].length() - 4));
                            }
                            else if (path.length == 4)
                            {
                                addStickerPack(path[2], path[3].substring(0, path[3].length() - 4));
                            }
                        }
                );

        orderedStickerPacks = new ArrayList<>(stickerPacks.values());
        orderedStickerPacks.sort((a, b) -> a == b ? 0 :
                a.name.equals(ALL_PACK_KEY) ? -5000 :
                b.name.equals(ALL_PACK_KEY) ? 5000 :
                a.name.equals(DEFAULT_PACK_KEY) ? -4000 :
                b.name.equals(DEFAULT_PACK_KEY) ? 4000:
                a.name.compareTo(b.name));

        loadStickerPack(stickerPacks.get(ALL_PACK_KEY));

        readjustComponents();

        super.init();
    }

    private void addStickerPack(String pack, String name) {
        if (((PlayerEntityAccess)MinecraftClient.getInstance().player).getStickerPackCollection().hasStickerPack(pack))
        {
            addSticker(pack, pack + "/" + name);
        }
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
    }

    public void loadStickerPack(StickerGroupWidget pack) {
        clearStickers();

        pack.select();
        for (var name : pack.stickerNames) {
            StickerWidget button = new StickerWidget(0, 0, name);

            String packName;
            if (name.indexOf('/') == -1)
            {
                packName = "default";
            }
            else
            {
                packName = name.substring(0, name.indexOf('/'));
            }

            button.setTooltip(Tooltip.of(
                    Text.translatable(
                            "stickers.nyakomod.sticker_tooltip",
                            Text.translatable("stickers.nyakomod.sticker." + name + ".title").formatted(Formatting.AQUA),
                            Text.translatable("stickers.nyakomod.pack." + packName + ".title").formatted(Formatting.WHITE)
                    ).formatted(Formatting.GRAY)
            ));
            this.addDrawableChild(button);

            stickers.add(button);
        }

        for (var stickerPack : orderedStickerPacks) {
            if (pack != stickerPack) {
                stickerPack.deselect();
            }
        }

        readjustComponents();
    }

    private void clearStickers() {
        for (var sticker : stickers) {
            this.remove(sticker);
        }
        stickers.clear();
    }

    private void readjustComponents() {
        readjustPacks();
        readjustStickers();
    }

    private void readjustPacks() {
        int totalHeight = (int) ((Math.ceil((float)stickerPacks.size())) * (32));
        int scrollMax = totalHeight - this.height + 32;

        if (scrollPacks < -scrollMax)
        {
            scrollPacks = -scrollMax;
        }

        if (scrollPacks > 0)
        {
            scrollPacks = 0;
        }

        // center if the entire thing fits in the screen
        if (scrollMax < 0)
        {
            scrollPacks = 0;
        }

        int index = 0;
        for (var pack : orderedStickerPacks)
        {
            pack.setX(24);
            pack.setY(64 + (index++ * (20)) + scrollPacks);

            pack.active = pack.getY() > 32;
            pack.visible = pack.getY() > 32;

        }
    }

    private void readjustStickers() {
        int stickersPerRow = Math.min((this.width - SIDEBAR_WIDTH) / (64 + 16), 6);
        int totalHeight = (int) ((Math.ceil((float)stickers.size() / (float)stickersPerRow)) * (64 + 16));
        int scrollMax = totalHeight - this.height + 16;

        if (scrollStickers < -scrollMax)
        {
            scrollStickers = -scrollMax;
        }

        if (scrollStickers > 0)
        {
            scrollStickers = 0;
        }

        // center if the entire thing fits in the screen
        if (scrollMax < 0)
        {
            scrollStickers = (this.height - 16 - totalHeight) / 2;
        }

        for (int index = 0; index < stickers.size(); index++)
        {
            int x = index % stickersPerRow;
            int y = index / stickersPerRow;

            StickerWidget sticker = stickers.get(index);

            sticker.active = true;
            sticker.setX(((this.width - (64 + 16) * stickersPerRow + SIDEBAR_WIDTH) / 2) + (x * (64 + 16)));
            sticker.setY(16 + (y * (64 + 16)) + scrollStickers);
        }
    }

    public StickerGroupWidget getStickerGroup(String pack) {
        if (!stickerPacks.containsKey(pack)) {
            var widget = new StickerGroupWidget(0, 0, pack);
            stickerPacks.put(pack, widget);
            this.addDrawableChild(widget);
        }

        return stickerPacks.get(pack);
    }

    public void addSticker(String pack, String name) {
        getStickerGroup(ALL_PACK_KEY).addSticker(name);
        getStickerGroup(pack).addSticker(name);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);

        super.render(context, mouseX, mouseY, delta);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        var label = Text.literal("Sticker Packs").setStyle(Style.EMPTY.withUnderline(true));
        context.drawText(textRenderer, label, 28, 20, 0xFFFFFFFF, true);

        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    public void renderBackground(DrawContext context) {
        context.fill(0, 0, this.width, this.height, 0x88000000);
        context.fill(0, 0, this.SIDEBAR_WIDTH - 16, this.height, 0x88000000);
        context.fill(0, 0, this.SIDEBAR_WIDTH - 16, 48, 0x88000000);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX < SIDEBAR_WIDTH) {
            scrollPacks += (int) (amount * 20);
            readjustPacks();
        } else {
            scrollStickers += (int) (amount * 16);
            readjustStickers();
        }

        return true;
    }
}
