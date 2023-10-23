package gay.nyako.nyakomod.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(MusicTracker.class)
public abstract class MusicTrackerMixin {

    @Final
    @Shadow
    private MinecraftClient client;

    @Redirect(method= "play(Lnet/minecraft/sound/MusicSound;)V", at=@At(value="INVOKE", target="Lnet/minecraft/client/sound/PositionedSoundInstance;music(Lnet/minecraft/sound/SoundEvent;)Lnet/minecraft/client/sound/PositionedSoundInstance;"))
    public PositionedSoundInstance spawnNewEntityAndPassengers(SoundEvent sound) {
        if (sound.getId().getPath().equals("music.credits")) {
            return PositionedSoundInstance.master(sound, 1, 0.75f);
        }
        return PositionedSoundInstance.music(sound);
    }

    @Redirect(method= "tick()V", at=@At(value="INVOKE", target="Lnet/minecraft/client/sound/MusicTracker;play(Lnet/minecraft/sound/MusicSound;)V"))
    private void play(MusicTracker musicTracker, MusicSound musicSound) {
        if (musicSound.getSound().getKey().get().getValue().getPath().equals("music.credits")) {
            if (this.client.currentScreen instanceof CreditsScreen creditsScreen) {
                if (creditsScreen.time > 100f) { // I don't know, just make sure to not play it again...
                    return;
                }
            }
        }
        musicTracker.play(musicSound);
    }

}
