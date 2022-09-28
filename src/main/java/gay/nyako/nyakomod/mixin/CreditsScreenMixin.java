package gay.nyako.nyakomod.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CreditsScreen.class)
public abstract class CreditsScreenMixin extends Screen {
    private static final Identifier VIGNETTE_TEXTURE = new Identifier("textures/misc/vignette.png");
    private static final Identifier QUEEN_TITLE_TEXTURE = new Identifier("nyakomod", "textures/gui/title/queen.png");

    protected CreditsScreenMixin(Text title) {
        super(title);
    }

    /**
     * @author NyakoFox
     * @reason It's kinda funny.
     */
    @Overwrite
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
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

        RenderSystem.disableBlend();

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