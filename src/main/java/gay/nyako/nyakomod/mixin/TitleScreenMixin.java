package gay.nyako.nyakomod.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    @Shadow @Final private RotatingCubeMapRenderer backgroundRenderer;
    @Shadow @Final private boolean doBackgroundFade;
    @Shadow private long backgroundFadeStart;
    @Unique
    private static final Identifier BACKGROUND = new Identifier("nyakomod", "textures/gui/title/background.png");

    @Unique
    private static final Identifier LOGO = new Identifier("nyakomod", "textures/gui/title/allybox.png");

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Redirect(method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/RotatingCubeMapRenderer;render(FF)V"))
    private void renderBackground(RotatingCubeMapRenderer instance, float delta, float alpha, DrawContext context, int mouseX, int mouseY, float delta2) {

        float f = this.doBackgroundFade ? (float)(Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0f : 1.0f;

        RenderSystem.enableBlend();
        context.setShaderColor(1.0f, 1.0f, 1.0f, this.doBackgroundFade ? (float)MathHelper.ceil(MathHelper.clamp(f, 0.0f, 1.0f)) : 1.0f);
        context.drawTexture(BACKGROUND, 0, 0, this.width, this.height, 0.0f, 0.0f, 960, 501, 960, 501);
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        RenderSystem.disableBlend();
        // RotatingCubeMapRenderer.render(delta, alpha);
    }

    @Redirect(method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/LogoDrawer;draw(Lnet/minecraft/client/gui/DrawContext;IF)V"))
    private void renderLogo(LogoDrawer instance, DrawContext context, int screenWidth, float alpha) {
        int width = 320/2;
        int height = 128/2;

        context.setShaderColor(1.0f, 1.0f, 1.0f, alpha);

        context.drawTexture(LOGO, (screenWidth / 2) - (width / 2), 25, width, height, 0.0f, 0.0f, width, height, width, height);

        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Inject(method = "initWidgetsNormal(II)V", at = @At(value = "TAIL"))
    private void initWidgetsNormal(int y, int spacingY, CallbackInfo ci) {
        for (Element element : this.children()) {
            if (element instanceof ClickableWidget widget)
            {
                if (widget.getY() == 156)
                {
                    widget.active = false;
                    widget.visible = false;
                }
                else
                {
                    widget.setY(widget.getY() + 20);
                }
            }
        }
    }

    /**
     * @author NyakoFox
     * @reason Lazy
     */
    @Overwrite
    public static CompletableFuture<Void> loadTexturesAsync(TextureManager textureManager, Executor executor) {
        return CompletableFuture.allOf(
                textureManager.loadTextureAsync(LogoDrawer.LOGO_TEXTURE, executor),
                textureManager.loadTextureAsync(LogoDrawer.EDITION_TEXTURE, executor),
                textureManager.loadTextureAsync(TitleScreen.PANORAMA_OVERLAY, executor),
                TitleScreen.PANORAMA_CUBE_MAP.loadTexturesAsync(textureManager, executor),
                textureManager.loadTextureAsync(BACKGROUND, executor),
                textureManager.loadTextureAsync(LOGO, executor)
        );
    }
}
