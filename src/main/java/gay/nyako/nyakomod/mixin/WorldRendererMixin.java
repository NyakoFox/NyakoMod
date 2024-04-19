package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoSoundEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Redirect(method= "processGlobalEvent(ILnet/minecraft/util/math/BlockPos;I)V", at=@At(value="INVOKE", target="Lnet/minecraft/client/world/ClientWorld;playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V"))
    public void redirect(ClientWorld clientWorld, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance, int eventId) {
        clientWorld.playSound(x, y, z, sound, category, volume, pitch, true);
        if (eventId == WorldEvents.ENDER_DRAGON_DIES) {
            // clientWorld.playSound(x, y, z, NyakoSoundEvents.WOLVES, category, volume, pitch, true);
        }
    }
    @Redirect(method= "processWorldEvent", at=@At(value="INVOKE", target="Lnet/minecraft/client/world/ClientWorld;playSoundAtBlockCenter(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V"))
    public void redirect(ClientWorld clientWorld, BlockPos pos, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance, int eventId, BlockPos pos2, int data) {
        if (eventId == WorldEvents.END_PORTAL_FRAME_FILLED) {
            float newPitch = switch (data) {
                case 0 -> 0.6f;
                case 1 -> 0.8f;
                case 2 -> 0.8f;
                case 3 -> 1.0f;
                case 4 -> 1.1f;
                case 5 -> 1.15f;
                case 6 -> 1.1f;
                case 7 -> 1.0f;
                case 8 -> 0.8f;
                case 9 -> 0.75f;
                case 10 -> 0.9f;
                case 11 -> 0.8f;
                default -> pitch;
            };

            clientWorld.playSoundAtBlockCenter(pos, sound, category, volume, newPitch, useDistance);
        }
        else
        {
            clientWorld.playSoundAtBlockCenter(pos, sound, category, volume, pitch, useDistance);
        }
    }
}