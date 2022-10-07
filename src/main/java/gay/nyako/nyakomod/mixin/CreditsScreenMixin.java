package gay.nyako.nyakomod.mixin;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import gay.nyako.nyakomod.NyakoSoundEvents;
import io.netty.channel.unix.IovArray;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.sound.MusicType;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

@Mixin(CreditsScreen.class)
public abstract class CreditsScreenMixin extends Screen {
    private static final Identifier VIGNETTE_TEXTURE = new Identifier("textures/misc/vignette.png");
    private static final Identifier QUEEN_TITLE_TEXTURE = new Identifier("nyakomod", "textures/gui/title/queen.png");
    private static final Identifier ALLYBOX_TITLE_TEXTURE = new Identifier("nyakomod", "textures/gui/title/allybox.png");
    private static final Identifier LUIGI_TITLE_TEXTURE = new Identifier("nyakomod", "textures/gui/title/luigi.png");

    private static final boolean DEBUG = false;

    public boolean playedSound = false;

    protected CreditsScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    public float time;

    @Shadow
    private float speed;

    @Shadow
    private List<OrderedText> credits;

    @Shadow
    private IntSet centeredLines;

    @Final
    @Shadow
    private boolean endCredits;

    @Shadow
    private int creditsHeight;

    @Shadow public abstract void close();

    @Override
    public boolean shouldCloseOnEsc() {
        return DEBUG;
        //return this.time > 1000f;
    }

    @ModifyVariable(method = "<init>(ZLjava/lang/Runnable;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static boolean injected(boolean endCredits) {
        return DEBUG || endCredits;
    }

    @Shadow
    private void readPoem(Reader reader) throws IOException {};

    @Shadow
    private void load(String id, CreditsScreen.CreditsReader reader) {};

    /**
     * @author NyakoFox
     * @reason Lol
     */
    @Overwrite
    public void init() {
        if (this.credits != null) {
            return;
        }
        playedSound = false;
        time = 0f;
        this.credits = Lists.newArrayList();
        this.centeredLines = new IntOpenHashSet();
        this.load("texts/end.txt", this::readPoem);
        this.creditsHeight = this.credits.size() * 12;

        if (!this.endCredits && !DEBUG) {
            this.close();
        }
    }

    /**
     * @author NyakoFox
     * @reason gotta make sure it advances at the right time
     */
    @Overwrite
    public void tick() {
        this.client.getMusicTracker().tick();
        this.client.getSoundManager().tick(false);
        if (this.time > 2150f && !playedSound) {
            client.getSoundManager().play(PositionedSoundInstance.master(NyakoSoundEvents.SUPER_LUIGI, 1.0f, 1.5f));
            playedSound = true;
        }
        if (this.time > 2400f) {
            this.close();
        }
    }

    /**
     * @author NyakoFox
     * @reason It's kinda funny.
     */
    @Overwrite
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int speedMult = 2;
        this.time += delta * (this.speed * (float) speedMult);

        int textureWidth = 1024;
        int textureHeight = 1024;

        float scale = 0.5f * (this.width / (float) textureWidth);

        this.fillGradient(matrices, 0, 0, this.width, this.height, 0xFF000000, 0xFF000000);

        matrices.push();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, QUEEN_TITLE_TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableBlend();
        matrices.translate(this.width / 2f, this.height / 2f, 0);
        matrices.scale(scale, scale, 0f);

        drawTexture(matrices, -(textureWidth / 2), -(textureHeight / 2), 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);

        matrices.pop();

        matrices.push();
        RenderSystem.enableBlend();


        int fadeAlpha = (int) MathHelper.clamp(255 - (255f * ((time / (float)speedMult) / 40)), 40, 255);

        if (time > 1700) {
            float offsetTime = time - 1700;
            fadeAlpha = (int) MathHelper.clamp(offsetTime * 4, 40, 255);
        }

        var color = ColorHelper.Argb.getArgb(fadeAlpha, 0, 0, 0);
        this.fillGradient(matrices, 0, 0, this.width, this.height, color, color);

        RenderSystem.disableBlend();

        matrices.pop();

        matrices.push();

        int i = this.width / 2 - 137;
        float f = -this.time;

        matrices.translate(0.0, f, 0.0);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        int k = this.height + 40 * speedMult;

        int logo_x = this.width / 2 - 160;
        int logo_y = this.height + 1080;

        RenderSystem.setShaderTexture(0, ALLYBOX_TITLE_TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableBlend();

        drawTexture(matrices, logo_x, logo_y, 0, 0, 160 * 2, 128, 160 * 2, 128);

        RenderSystem.disableBlend();

        for (int l = 0; l < this.credits.size(); ++l) {
            float g;
            if (l == this.credits.size() - 1 && (g = (float)k + f - (float)(this.height / 2 - 6)) < 0.0f) {
                matrices.translate(0.0, -g, 0.0);
            }
            if ((float)k + f + 12.0f + 8.0f > 0.0f && (float)k + f < (float)this.height) {
                OrderedText orderedText = this.credits.get(l);
                if (this.centeredLines.contains(l)) {
                    this.textRenderer.drawWithShadow(matrices, orderedText, (float)(i + (274 - this.textRenderer.getWidth(orderedText)) / 2), (float)k, 0xFFFFFF);
                } else {
                    this.textRenderer.drawWithShadow(matrices, orderedText, (float)i, (float)k, 0xFFFFFF);
                }
            }
            k += 12;
        }

        matrices.pop();

        RenderSystem.enableBlend();

        var fadeAlpha2 = 0;
        if (time > 2050) {
            float offsetTime = time - 2050;
            fadeAlpha2 = (int) MathHelper.clamp(offsetTime * 4, 0, 255);
        }

        var fadeColor = ColorHelper.Argb.getArgb(fadeAlpha2, 0, 0, 0);
        this.fillGradient(matrices, 0, 0, this.width, this.height, fadeColor, fadeColor);

        RenderSystem.disableBlend();


        if (time > 2150) {
            int textureWidth2 = 900;
            int textureHeight2 = 57;

            float scale2 = (this.width / (float) textureWidth2);

            this.fillGradient(matrices, 0, 0, this.width, this.height, 0xFF000000, 0xFF000000);

            matrices.push();
            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            RenderSystem.setShaderTexture(0, LUIGI_TITLE_TEXTURE);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.enableBlend();
            matrices.translate(this.width / 2f, this.height / 2f, 0);
            matrices.scale(scale2, scale2, 0f);

            drawTexture(matrices, -(textureWidth2 / 2), -(textureHeight2 / 2), 0, 0, textureWidth2, textureHeight2, textureWidth2, textureHeight2);

            matrices.pop();

            var timeOffset = time - 2150;
            // between 0 and 1
            var progress = 1f - MathHelper.clamp(timeOffset / 2.5, 0f, 1f);

            if (this.time > 2300f) {
                progress = 1f;
            }

            var fadeColor2 = ColorHelper.Argb.getArgb((int) MathHelper.clamp(progress * 255, 0, 255), 0, 0, 0);
            this.fillGradient(matrices, 0, 0, this.width, this.height, fadeColor2, fadeColor2);
        }

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, VIGNETTE_TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR);
        int l = this.width;
        int m = this.height;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(0.0, m, this.getZOffset()).texture(0.0f, 1.0f).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferBuilder.vertex(l, m, this.getZOffset()).texture(1.0f, 1.0f).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferBuilder.vertex(l, 0.0, this.getZOffset()).texture(1.0f, 0.0f).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferBuilder.vertex(0.0, 0.0, this.getZOffset()).texture(0.0f, 0.0f).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        tessellator.draw();
        RenderSystem.disableBlend();


        if (DEBUG) {
            this.textRenderer.drawWithShadow(matrices, Text.literal(String.valueOf(this.time)), 20f, 20f, 0xFFFFFF);
        }

        super.render(matrices, mouseX, mouseY, delta);
    }

}