package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.NyakoModItem;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Redirect(method="onItemPickupAnimation(Lnet/minecraft/network/packet/s2c/play/ItemPickupAnimationS2CPacket;)V", at=@At(value="INVOKE", target="Lnet/minecraft/client/world/ClientWorld;playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V"))
    private void redirect(ClientWorld world, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance, ItemPickupAnimationS2CPacket packet) {
        Entity entity = world.getEntityById(packet.getEntityId());

        if (entity instanceof ItemEntity) {
            ItemEntity itemEntity = (ItemEntity)entity;
            ItemStack itemStack = itemEntity.getStack();
            if (itemStack.isOf(NyakoModItem.COPPER_COIN_ITEM) ||
                itemStack.isOf(NyakoModItem.GOLD_COIN_ITEM) ||
                itemStack.isOf(NyakoModItem.EMERALD_COIN_ITEM) ||
                itemStack.isOf(NyakoModItem.DIAMOND_COIN_ITEM) ||
                itemStack.isOf(NyakoModItem.NETHERITE_COIN_ITEM)) {
                world.playSound(x, y, z, NyakoMod.COIN_COLLECT_SOUND_EVENT, category, 0.7f, pitch, useDistance);
                return;
            }
        }
        world.playSound(x, y, z, sound, category, volume, pitch, useDistance);
    }

    // entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, (this.random.nextFloat() - this.random.nextFloat()) * 1.4f + 2.0f, false
}