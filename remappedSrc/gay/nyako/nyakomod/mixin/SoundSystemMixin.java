package gay.nyako.nyakomod.mixin;

import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SoundSystem.class)
public abstract class SoundSystemMixin {

    @Redirect(method = "getAdjustedPitch(Lnet/minecraft/client/sound/SoundInstance;)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F"))
    private float injected(float value, float min, float max) {
        return value;
    }

}