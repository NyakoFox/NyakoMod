package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoModSoundEvents;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
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
            clientWorld.playSound(x, y, z, NyakoModSoundEvents.WOLVES, category, volume, pitch, true);
        }
        return;
    }
}