package gay.nyako.nyakomod;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class StaffOfSmitingItem extends Item {

    public StaffOfSmitingItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target instanceof PlayerEntity) {
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            ServerPlayNetworking.send((ServerPlayerEntity) target, NyakoMod.PLAYER_SMITE_PACKET_ID, passedData);
        }
        LightningEntity lightningBolt = new LightningEntity(EntityType.LIGHTNING_BOLT, target.world);
        lightningBolt.setCosmetic(true);
        lightningBolt.setPos(target.getX(), target.getY(), target.getZ());
        target.world.spawnEntity(lightningBolt);
        target.addVelocity(0d, 5d, 0d);
        return true;
    };
}
