package gay.nyako.nyakomod.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import gay.nyako.nyakomod.access.ClientPlayerEntityAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {


    @Shadow @Final private MinecraftClient client;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V"), slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;render(Lnet/minecraft/client/gui/DrawContext;F)V"),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getOverlay()Lnet/minecraft/client/gui/screen/Overlay;")
    ))
    private void injected(float tickDelta, long startTime, boolean tick, CallbackInfo ci, @Local DrawContext drawContext) {
        // Render the flashbang!

        float strength = ((ClientPlayerEntityAccess) (client.player)).getFlashbangStrength();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        drawContext.fill(0, 0, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight(), ColorHelper.Argb.getArgb((int) (strength * 255), 255, 255, 255));

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }

}
