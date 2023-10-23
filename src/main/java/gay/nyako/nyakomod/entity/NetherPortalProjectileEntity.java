package gay.nyako.nyakomod.entity;

import gay.nyako.nyakomod.NyakoEntities;
import gay.nyako.nyakomod.NyakoItems;
import gay.nyako.nyakomod.NyakoSoundEvents;
import gay.nyako.nyakomod.utils.NyakoUtils;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;


public class NetherPortalProjectileEntity extends ProjectileEntity implements FlyingItemEntity {
    private static final TrackedData<ItemStack> ITEM = DataTracker.registerData(NetherPortalProjectileEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    private static final TrackedData<OptionalInt> SHOOTER_ENTITY_ID = DataTracker.registerData(NetherPortalProjectileEntity.class, TrackedDataHandlerRegistry.OPTIONAL_INT);
    private int life;
    private int lifeTime;
    @Nullable
    private LivingEntity shooter;

    public NetherPortalProjectileEntity(EntityType<? extends NetherPortalProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public NetherPortalProjectileEntity(World world, double x, double y, double z, ItemStack stack) {
        super(NyakoEntities.NETHER_PORTAL, world);
        this.life = 0;
        this.setPosition(x, y, z);
        if (!stack.isEmpty() && stack.hasNbt()) {
            this.dataTracker.set(ITEM, stack.copy());
        }
        this.setVelocity(this.random.nextTriangular(0.0, 0.002297), 0.05, this.random.nextTriangular(0.0, 0.002297));
        this.lifeTime = 30;
    }

    public NetherPortalProjectileEntity(World world, ItemStack stack, double x, double y, double z) {
        this(world, x, y, z, stack);
    }

    public NetherPortalProjectileEntity(World world, ItemStack stack, Entity entity, double x, double y, double z) {
        this(world, stack, x, y, z);
        this.setOwner(entity);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(ITEM, ItemStack.EMPTY);
        this.dataTracker.startTracking(SHOOTER_ENTITY_ID, OptionalInt.empty());
    }

    @Override
    public boolean shouldRender(double distance) {
        return distance < 4096.0 && !this.wasShotByEntity();
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return super.shouldRender(cameraX, cameraY, cameraZ) && !this.wasShotByEntity();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.wasShotByEntity()) {
            if (this.shooter == null) {
                this.dataTracker.get(SHOOTER_ENTITY_ID).ifPresent(id -> {
                    Entity entity = this.getWorld().getEntityById(id);
                    if (entity instanceof LivingEntity) {
                        this.shooter = (LivingEntity)entity;
                    }
                });
            }
            if (this.shooter != null) {
                this.setPosition(this.shooter.getX(), this.shooter.getY(), this.shooter.getZ());
                this.setVelocity(this.shooter.getVelocity());
            }
        } else {
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity());
        }
        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        if (!this.noClip) {
            this.onCollision(hitResult);
            this.velocityDirty = true;
        }
        this.updateRotation();
        if (this.life == 0 && !this.isSilent()) {
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), NyakoSoundEvents.NETHER_PORTAL_LAUNCH, SoundCategory.AMBIENT, 3.0f, 1.0f);
        }
        ++this.life;
        if (this.getWorld().isClient) {
            this.getWorld().addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, -this.getVelocity().y * 0.5, this.random.nextGaussian() * 0.05);
        }
        if (!this.getWorld().isClient && this.life > this.lifeTime) {
            this.remove(RemovalReason.DISCARDED);
            this.discard();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        Entity entity = entityHitResult.getEntity();

        var owner = this.getOwner();

        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }

        if (NyakoUtils.blockedByShield(livingEntity, this.getPos())) {
            this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, 1.0F, 1.0F, true);
            dropStack(getStack());
            this.remove(RemovalReason.DISCARDED);
            this.discard();
            return;
        }

        if (livingEntity != owner && livingEntity instanceof PlayerEntity && owner instanceof ServerPlayerEntity && !this.isSilent()) {
            ((ServerPlayerEntity)owner).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, 0));
        }

        this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));

        if (this.getWorld().isClient) {
            return;
        }

        MinecraftServer minecraftServer = getWorld().getServer();
        if (minecraftServer == null) return;

        if (getWorld().getRegistryKey() == World.END) {
            getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 5f, World.ExplosionSourceType.NONE);
            return;
        }

        ServerWorld serverWorld2 = minecraftServer.getWorld(getWorld().getRegistryKey() == World.NETHER ? World.OVERWORLD : World.NETHER);
        entity.dismountVehicle();
        if (serverWorld2 != null && minecraftServer.isNetherAllowed()) {
            getWorld().getProfiler().push("portal");
            entity.lastNetherPortalPosition = entity.getBlockPos().toImmutable();
            entity.resetPortalCooldown();
            entity.moveToWorld(serverWorld2);
            getWorld().getProfiler().pop();
        }

        this.remove(RemovalReason.DISCARDED);
        this.discard();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        BlockPos blockPos = new BlockPos(blockHitResult.getBlockPos());
        this.getWorld().getBlockState(blockPos).onEntityCollision(this.getWorld(), blockPos, this);
        if (!this.getWorld().isClient()) {
            dropStack(getStack());
            this.remove(RemovalReason.DISCARDED);
            this.discard();
        }
        super.onBlockHit(blockHitResult);
    }

    private boolean wasShotByEntity() {
        return this.dataTracker.get(SHOOTER_ENTITY_ID).isPresent();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Life", this.life);
        nbt.putInt("LifeTime", this.lifeTime);
        ItemStack itemStack = this.dataTracker.get(ITEM);
        if (!itemStack.isEmpty()) {
            nbt.put("PortalItem", itemStack.writeNbt(new NbtCompound()));
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.life = nbt.getInt("Life");
        this.lifeTime = nbt.getInt("LifeTime");
        ItemStack itemStack = ItemStack.fromNbt(nbt.getCompound("PortalItem"));
        if (!itemStack.isEmpty()) {
            this.dataTracker.set(ITEM, itemStack);
        }
    }

    @Override
    public ItemStack getStack() {
        ItemStack itemStack = this.dataTracker.get(ITEM);
        return itemStack.isEmpty() ? new ItemStack(NyakoItems.NETHER_PORTAL_STRUCTURE) : itemStack;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }
}
