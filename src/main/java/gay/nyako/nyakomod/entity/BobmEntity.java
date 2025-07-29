package gay.nyako.nyakomod.entity;

import gay.nyako.nyakomod.NyakoEntities;
import gay.nyako.nyakomod.NyakoItems;
import gay.nyako.nyakomod.NyakoNetworking;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class BobmEntity extends BombEntity {
    public BobmEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    public BobmEntity(World world, LivingEntity owner) {
        super(NyakoEntities.BOBM, owner, world);
    }

    public BobmEntity(World world, double x, double y, double z) {
        super(NyakoEntities.BOBM, x, y, z, world);
    }

    @Override
    protected void explode(World world) {
        float power = 8F;
        world.createExplosion(this, this.getDamageSources().explosion(this, getOwner()), null, this.getPos(), power, true, World.ExplosionSourceType.TNT);

        float flashbangMult = 4.0F;

        float q = power * 2.0F * flashbangMult;
        int k = MathHelper.floor(getX() - (double)q - 1.0);
        int lx = MathHelper.floor(getX() + (double)q + 1.0);
        int r = MathHelper.floor(getY() - (double)q - 1.0);
        int s = MathHelper.floor(getY() + (double)q + 1.0);
        int t = MathHelper.floor(getZ() - (double)q - 1.0);
        int u = MathHelper.floor(getZ() + (double)q + 1.0);

        List<Entity> list = world.getOtherEntities(this, new Box(k, r, t, lx, s, u));

        for (int v = 0; v < list.size(); v++) {
            Entity entity = list.get(v);
            if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                if (!serverPlayerEntity.isImmuneToExplosion()) {
                    double w = Math.sqrt(serverPlayerEntity.squaredDistanceTo(getPos())) / (double) q;
                    PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
                    buffer.writeDouble(w);
                    ServerPlayNetworking.send(serverPlayerEntity, NyakoNetworking.FLASHBANG, buffer);
                }
            }
        }
    }

    @Override
    protected Item getDefaultItem() {
        return NyakoItems.BOBM;
    }
}
