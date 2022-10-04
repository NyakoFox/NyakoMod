package gay.nyako.nyakomod.mixin;

import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MusicTracker.class)
public abstract class MusicTrackerMixin {

    @Redirect(method= "play(Lnet/minecraft/sound/MusicSound;)V", at=@At(value="INVOKE", target="Lnet/minecraft/client/sound/PositionedSoundInstance;music(Lnet/minecraft/sound/SoundEvent;)Lnet/minecraft/client/sound/PositionedSoundInstance;"))
    public PositionedSoundInstance spawnNewEntityAndPassengers(SoundEvent sound) {
        if (sound.getId().getPath() == "music.credits") {
            return PositionedSoundInstance.master(sound, 1, 0.75f);
        }
        return PositionedSoundInstance.music(sound);
    }
}
