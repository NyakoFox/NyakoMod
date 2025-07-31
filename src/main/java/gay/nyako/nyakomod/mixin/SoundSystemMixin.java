package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.access.ClientPlayerEntityAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundSystem.class)
public abstract class SoundSystemMixin {

    /*
    @Redirect(method = "getAdjustedPitch(Lnet/minecraft/client/sound/SoundInstance;)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F"))
    private float injected(float value, float min, float max) {
        return value;
    }
    */

    @Inject(method = "getAdjustedVolume(FLnet/minecraft/sound/SoundCategory;)F", at = @At("RETURN"), cancellable = true)
    private void injected(CallbackInfoReturnable<Float> cir) {
        if (MinecraftClient.getInstance().player != null) {
            ClientPlayerEntityAccess player = (ClientPlayerEntityAccess) MinecraftClient.getInstance().player;
            float value = cir.getReturnValue();
            cir.setReturnValue(value * (1 - player.getFlashbangProgress()));
        }
    }
}
