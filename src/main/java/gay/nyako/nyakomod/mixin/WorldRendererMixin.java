package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Redirect(method= "processGlobalEvent(ILnet/minecraft/util/math/BlockPos;I)V", at=@At(value="INVOKE", target="Lnet/minecraft/client/world/ClientWorld;playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V"))
    public void redirect(ClientWorld clientWorld, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance, int eventId) {
        clientWorld.playSound(x, y, z, sound, category, volume, pitch, true);
        if (eventId == WorldEvents.ENDER_DRAGON_DIES) {
            clientWorld.playSound(x, y, z, NyakoMod.WOLVES_SOUND_EVENT, category, volume, pitch, true);
        }
        return;
    }
}