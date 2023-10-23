package gay.nyako.nyakomod.mixin;

import com.mojang.authlib.GameProfile;
import gay.nyako.nyakomod.access.BoatEntityAccess;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.encryption.PlayerPublicKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
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
    private void injected(CallbackInfo ci) {
        ((BoatEntityAccess) this.getVehicle()).setPressingJump(input.jumping);
    }
}
