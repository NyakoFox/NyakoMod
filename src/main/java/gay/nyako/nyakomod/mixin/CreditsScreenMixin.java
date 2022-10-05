package gay.nyako.nyakomod.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.netty.channel.unix.IovArray;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
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

import java.util.List;

@Mixin(CreditsScreen.class)
public abstract class CreditsScreenMixin extends Screen {
    private static final Identifier VIGNETTE_TEXTURE = new Identifier("textures/misc/vignette.png");
    private static final Identifier QUEEN_TITLE_TEXTURE = new Identifier("nyakomod", "textures/gui/title/queen.png");

    protected CreditsScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    private float time;

    @Shadow
    private float speed;

    @Shadow
    private List<OrderedText> credits;

    @Shadow
    private IntSet centeredLines;

    @Final
    @Shadow
    private boolean endCredits;

    @Override
    public boolean shouldCloseOnEsc() {
        return this.time > 60f;
    }

    /*@ModifyVariable(method = "<init>(ZLjava/lang/Runnable;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static boolean injected(boolean endCredits) {
        return true;
    }*/

    @Inject(method = "init()V", at = @At("TAIL"))
    private void injected(CallbackInfo ci) {
        if (!this.endCredits) {
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

        var color = ColorHelper.Argb.getArgb((int) MathHelper.clamp(255 - (255f * ((time / (float)speedMult) / 40)), 40, 255), 0, 0, 0);
        this.fillGradient(matrices, 0, 0, this.width, this.height, color, color);

        RenderSystem.disableBlend();

        matrices.pop();

        matrices.push();

        int i = this.width / 2 - 137;
        float f = -this.time;

        matrices.translate(0.0, f, 0.0);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        int k = this.height + 40 * speedMult;
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
        super.render(matrices, mouseX, mouseY, delta);
    }

}