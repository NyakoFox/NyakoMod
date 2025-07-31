package gay.nyako.nyakomod.mixin;

import com.mojang.authlib.GameProfile;
import gay.nyako.nyakomod.access.BoatEntityAccess;
import gay.nyako.nyakomod.access.ClientPlayerEntityAccess;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements ClientPlayerEntityAccess {
    private float flashbangStrength = 0;
    private float flashbangDistance = 0;

    @Shadow
    public Input input;

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return null;
    }

    @Inject(method = "tickRiding()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;setInputs(ZZZZ)V", shift = At.Shift.AFTER))
    private void nyakomod$tickRiding(CallbackInfo ci) {
        ((BoatEntityAccess) this.getVehicle()).setPressingJump(input.jumping);
    }

    @Inject(method = "tickMovement()V", at = @At("HEAD"))
    private void nyakomod$tickMovement(CallbackInfo ci) {
        if (flashbangStrength > 0) {
            float speed = (float) MathHelper.lerp(flashbangDistance, 0.001, 0.1);

            flashbangStrength -= speed;
            if (flashbangStrength < 0) {
                flashbangStrength = 0;
            }
        }
    }

    @Override
    public float getFlashbangStrength() {
        return flashbangStrength;
    }

    @Override
    public void setFlashbangStrength(float strength) {
        flashbangStrength = strength;
    }

    @Override
    public float getFlashbangDistance() {
        return flashbangDistance;
    }

    @Override
    public void setFlashbangDistance(float flashbangDistance) {
        this.flashbangDistance = flashbangDistance;
    }

    @Override
    public float getFlashbangProgress() {
        return MathHelper.clamp(flashbangStrength, 0, 1);
    }
}
