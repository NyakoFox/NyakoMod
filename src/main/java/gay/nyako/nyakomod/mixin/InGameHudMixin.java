package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.access.PlayerEntityAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
@Unique
private static final Identifier CUSTOM_GUI_ICONS_TEXTURE = new Identifier("nyakomod", "textures/gui/icons.png");
    @Shadow
    protected abstract PlayerEntity getCameraPlayer();

    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(method = "renderStatusBars(Lnet/minecraft/client/gui/DrawContext;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getProfiler()Lnet/minecraft/util/profiler/Profiler;"))
    private void injected(DrawContext context, CallbackInfo ci) {

        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity == null) {
            return;
        }

        var scaledWidth = client.getWindow().getScaledWidth();
        var scaledHeight = client.getWindow().getScaledHeight();

        int xpos = scaledWidth / 2 + 91;

        int maxAir = playerEntity.getMaxAir();
        int currentAir = Math.min(playerEntity.getAir(), maxAir);

        var offset = (scaledHeight - 39) - 10;
        if (playerEntity.isSubmergedIn(FluidTags.WATER) || currentAir < maxAir) {
            offset -= 10;
        }

        int maxMilk = 20;
        int currentMilk = ((PlayerEntityAccess) playerEntity).getMilk();

        var maxIcons = 10;
        float iconCount = (float) currentMilk / (float) maxMilk * maxIcons;

        for (int bottle = 0; bottle < maxIcons; bottle++) {
            var diff = (bottle - iconCount) + 1;
            if (diff > 0.4 && diff < 0.6) {
                // Draw half
                context.drawTexture(CUSTOM_GUI_ICONS_TEXTURE, xpos - bottle * 8 - 9, offset, 9, 0, 9, 9);
            } else if (diff >= 0.6) {
                // Draw empty
                context.drawTexture(CUSTOM_GUI_ICONS_TEXTURE, xpos - bottle * 8 - 9, offset, 18, 0, 9, 9);
            } else {
                // Draw full
                context.drawTexture(CUSTOM_GUI_ICONS_TEXTURE, xpos - bottle * 8 - 9, offset, 0, 0, 9, 9);
            }
        }
    }
}