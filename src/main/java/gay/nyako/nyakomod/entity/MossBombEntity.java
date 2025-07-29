package gay.nyako.nyakomod.entity;

import gay.nyako.nyakomod.NyakoEntities;
import gay.nyako.nyakomod.NyakoItems;
import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.UndergroundConfiguredFeatures;

public class MossBombEntity extends BombEntity {
    private static final int EXPLOSION_Y_LENGTH = 4;

    public MossBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    public MossBombEntity(World world, LivingEntity owner) {
        super(NyakoEntities.MOSS_BOMB, owner, world);
    }

    public MossBombEntity(World world, double x, double y, double z) {
        super(NyakoEntities.MOSS_BOMB, x, y, z, world);
    }

    @Override
    protected void explode(World world) {
        world.createExplosion(this, this.getDamageSources().explosion(this, getOwner()), null, this.getPos(), 2.2F, false, World.ExplosionSourceType.NONE);

        // Mostly from https://github.com/DawnTeamMC/Promenade/blob/main/src/main/java/fr/hugman/promenade/entity/LushCreeperEntity.java

        ServerWorld serverWorld = (ServerWorld) world;

        Registry<ConfiguredFeature<?, ?>> registry = world.getRegistryManager().get(RegistryKeys.CONFIGURED_FEATURE);
        for (int i = 0; i < EXPLOSION_Y_LENGTH; i++) {
            BlockPos pos = getBlockPos().down(i);
            if (this.getWorld().getBlockState(pos).isSolidBlock(this.getWorld(), pos)) {
                registry.get(NyakoMod.BOMB_MOSS_PATCH).generate(serverWorld, serverWorld.getChunkManager().getChunkGenerator(), random, pos.up());
                break;
            }
        }
        for (int i = 0; i < EXPLOSION_Y_LENGTH; i++) {
            BlockPos pos = getBlockPos().up(i);
            if (this.getWorld().getBlockState(pos).isSolidBlock(this.getWorld(), pos)) {
                registry.get(NyakoMod.BOMB_MOSS_PATCH_CEILING).generate(serverWorld, serverWorld.getChunkManager().getChunkGenerator(), random, pos.down());
                break;
            }
        }
    }

    @Override
    protected Item getDefaultItem() {
        return NyakoItems.MOSS_BOMB;
    }
}
