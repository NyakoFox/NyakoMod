package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.access.DragonFireballEntityAccess;
import gay.nyako.nyakomod.utils.CunkCoinUtils;
import gay.nyako.nyakomod.NyakoItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Map;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin extends MobEntity {
    @Shadow @Final @Nullable private EnderDragonFight fight;

    @Shadow @Final public EnderDragonPart head;

    @Shadow public abstract PhaseManager getPhaseManager();

    @Shadow public abstract boolean damagePart(EnderDragonPart part, DamageSource source, float amount);

    @Shadow @Final private EnderDragonPart body;
    int attack = -1;
    int attackTimer = 20 * 30;
    boolean wasPerched = false;
    PlayerEntity target = null;

    protected EnderDragonEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initInject(CallbackInfo ci) {
        this.setCustomName(Text.literal("Jender Jragon"));
    }

    private PlayerEntity pickTarget() {
        if (this.getPhaseManager().getCurrent().isSittingOrHovering()) {
            // If the dragon is sitting or hovering, randomly pick players who aren't within 16 blocks,
            // but not further than 64 blocks.
            // If no player is found, just pick a random player... within 96 blocks.

            double maxDistance = 64;
            double minDistance = 16;
            double maxDistanceHard = 96;
            ArrayList<PlayerEntity> possibleTargets = new ArrayList<>();

            // Loop through all players and check their distances
            // If they're in the correct range, add them to a list
            for (PlayerEntity player : this.world.getPlayers()) {
                if (player.squaredDistanceTo(this) > maxDistance * maxDistance) {
                    continue;
                }
                if (player.squaredDistanceTo(this) < minDistance * minDistance) {
                    continue;
                }
                possibleTargets.add(player);
            }

            // Didn't find any in the range
            // Loop through all players again and check their distances
            // If they're within the hard max, add them to the list
            if (possibleTargets.isEmpty()) {
                for (PlayerEntity player : this.world.getPlayers()) {
                    if (player.squaredDistanceTo(this) > maxDistanceHard * maxDistanceHard) {
                        continue;
                    }
                    possibleTargets.add(player);
                }
            }

            // Still nobody, so nobody is alive
            if (possibleTargets.isEmpty()) {
                return null;
            }

            // Pick a random player from the list
            return possibleTargets.get(this.random.nextInt(possibleTargets.size()));
        } else {
            ArrayList<PlayerEntity> possibleTargets = new ArrayList<>();
            for (PlayerEntity player : this.world.getPlayers()) {
                if (player.squaredDistanceTo(this) > 128 * 128) {
                    continue;
                }
                possibleTargets.add(player);
            }
            var randomPlayer = possibleTargets.get(this.random.nextInt(possibleTargets.size()));

            var closestPlayer = this.world.getClosestPlayer(this, 128);
            closestPlayer = closestPlayer == null ? randomPlayer : closestPlayer;

            return switch (this.attack) {
                case 0, 1, 2, 3 -> randomPlayer;
                default -> closestPlayer;
            };
        }
    }

    /**
     * @author NyakoFox
     * @reason If explosive, drop coins
     */
    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!this.world.isClient) {
            if (source.isExplosive() && amount >= 0.01f) {
                for (int i = 0; i < this.random.nextBetween(1, 4); i++) {
                    var stack = dropStack(new ItemStack(NyakoItems.COPPER_COIN, 2));
                    if (stack != null) {
                        stack.setVelocity(stack.getVelocity().multiply(0.5, 0.5, 0.5));
                    }
                    var stack2 = dropStack(new ItemStack(NyakoItems.DRAGON_SCALE, 1));
                    if (stack2 != null) {
                        stack2.setVelocity(stack2.getVelocity().multiply(0.5, 0.5, 0.5));
                    }

                }
            }
            return this.damagePart(this.body, source, amount);
        }
        return false;
    }

    private void onAttackChanged() {
        switch (this.attack) {
            case 0:
                // Don't do anything, burner attack
                attackTimer = 20 * 5;
                break;
            case 1:
                attackTimer = 20 * 4;
                if (!this.isSilent()) {
                    this.world.syncWorldEvent(null, WorldEvents.ENDER_DRAGON_SHOOTS, this.getBlockPos(), 0);
                }
                // Circle around the target
                var fireballs = 12;
                var radius = 8;
                var angle = 360 / fireballs;
                var pos = target.getPos();

                for (int i = 0; i < fireballs; i++) {
                    var x = pos.x + radius * MathHelper.cos((float) Math.toRadians(angle * i));
                    var y = pos.y + 64 + (i * 2);
                    var z = pos.z + radius * MathHelper.sin((float) Math.toRadians(angle * i));

                    var fireball = new DragonFireballEntity(world, this, 0, (target.getBodyY(0.5) - y * 0.5), 0);
                    fireball.setVelocity(0, 0, 0);
                    fireball.refreshPositionAndAngles(x, y, z, 0.0f, 0.0f);
                    ((DragonFireballEntityAccess) fireball).setFromAttack(true);
                    world.spawnEntity(fireball);
                }
                break;
            case 2:
                attackTimer = (80 * random.nextBetween(2, 6)) + 1;
                break;
            case 3:
                attackTimer = (80 * random.nextBetween(2, 6)) + 1;
                break;
            default:
                attackTimer = 1;
                break;
        }
    }

    private void tickAttack() {
        switch (this.attack) {
            case 0:
                // Do nothing
                break;
            case 1:
                // Do nothing
                break;
            case 2:
                if (attackTimer % 80 == 0) {
                    target = pickTarget();
                    if (target == null || target.isDead()) {
                        attackTimer = 0;
                        return;
                    }
                    if (!this.isSilent()) {
                        this.world.syncWorldEvent(null, WorldEvents.ENDER_DRAGON_SHOOTS, this.getBlockPos(), 0);
                    }
                    double spawnX = target.getX() + (this.world.random.nextDouble() - 0.5) * 16.0;
                    double spawnY = target.getY() + (double) (this.world.random.nextInt(16) + 64);
                    double spawnZ = target.getZ() + (this.world.random.nextDouble() - 0.5) * 16.0;

                    double directionX = target.getX() - spawnX;
                    double directionY = target.getBodyY(0.5) - spawnY;
                    double directionZ = target.getZ() - spawnZ;

                    directionX *= 0.5;
                    directionY *= 0.5;
                    directionZ *= 0.5;

                    DragonFireballEntity dragonFireballEntity = new DragonFireballEntity(this.world, this, directionX, directionY, directionZ);
                    dragonFireballEntity.refreshPositionAndAngles(spawnX, spawnY, spawnZ, 0.0f, 0.0f);
                    ((DragonFireballEntityAccess) dragonFireballEntity).setFromAttack(true);
                    this.world.spawnEntity(dragonFireballEntity);
                }
                break;
            case 3:
                if (attackTimer % 80 > 40) {
                    if (attackTimer % 4 == 0) {
                        if (!this.isSilent()) {
                            this.world.syncWorldEvent(null, WorldEvents.ENDER_DRAGON_SHOOTS, this.getBlockPos(), 0);
                        }

                        Vec3d vec3d3 = this.getRotationVec(1.0f);
                        double spawnX = this.head.getX() - vec3d3.x;
                        double spawnY = this.head.getBodyY(0.5) + 0.5;
                        double spawnZ = this.head.getZ() - vec3d3.z;
                        double directionX = this.target.getX() - spawnX;
                        double directionY = this.target.getBodyY(0.5) - spawnY;
                        double directionZ = this.target.getZ() - spawnZ;
                        // Adjust directionX, directionY and directionZ to slow it down a bit
                        directionX *= 0.5;
                        directionY *= 0.5;
                        directionZ *= 0.5;

                        DragonFireballEntity dragonFireballEntity = new DragonFireballEntity(this.world, this, directionX, directionY, directionZ);
                        dragonFireballEntity.refreshPositionAndAngles(spawnX, spawnY, spawnZ, 0.0f, 0.0f);
                        ((DragonFireballEntityAccess) dragonFireballEntity).setFromAttack(true);
                        this.world.spawnEntity(dragonFireballEntity);
                    }
                }
                break;
        }
    }

    @Inject(method = "tickMovement()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;tickWithEndCrystals()V", shift = At.Shift.AFTER))
    private void injected(CallbackInfo ci) {
        attackTimer--;

        if (!world.isClient()) {
            if (this.getPhaseManager().getCurrent().isSittingOrHovering() != wasPerched) {
                wasPerched = this.getPhaseManager().getCurrent().isSittingOrHovering();
                if (wasPerched) {
                    // Choose new attack since the dragon is perching
                    attackTimer = 0;
                }
            }
            if (attackTimer <= 0 || target == null || target.isDead()) {
                var oldAttack = attack;
                attack = this.world.random.nextBetween(0, 3);
                while (oldAttack == 0 && attack == 0) {
                    attack = this.world.random.nextBetween(0, 3);
                }

                target = pickTarget();

                if (target == null) {
                    attack = 0;
                }

                onAttackChanged();
            } else {
                tickAttack();
            }
        }
    }

    /**
     * @author NyakoFox
     * @reason Change attributes
     */
    @Overwrite
    public static DefaultAttributeContainer.Builder createEnderDragonAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 400.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.20);
    }

    @Redirect(method= "updatePostDeath()V", at=@At(value="INVOKE", target="Lnet/minecraft/entity/ExperienceOrbEntity;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;I)V"))
    public void redirect(ServerWorld world, Vec3d pos, int amount) {
        ExperienceOrbEntity.spawn(world, pos, amount);

        var stack2 = dropStack(new ItemStack(NyakoItems.DRAGON_SCALE, random.nextBetween(1, 5)));
        if (stack2 != null) {
            stack2.setVelocity(stack2.getVelocity().multiply(0.5, 0.5, 0.5));
        }

        float cunkFactor = 2f * world.getServer().getPlayerManager().getCurrentPlayerCount();
        if (fight != null && fight.hasPreviouslyKilled()) {
            var players = world.getPlayers().size();
            cunkFactor = 7.5f * players;
        }

        Map<CunkCoinUtils.CoinValue, Integer> map = CunkCoinUtils.valueToSplit(MathHelper.ceil(amount * cunkFactor));

        Integer copper = map.get(CunkCoinUtils.CoinValue.COPPER);
        Integer gold = map.get(CunkCoinUtils.CoinValue.GOLD);
        Integer emerald = map.get(CunkCoinUtils.CoinValue.EMERALD);
        Integer diamond = map.get(CunkCoinUtils.CoinValue.DIAMOND);
        Integer netherite = map.get(CunkCoinUtils.CoinValue.NETHERITE);

        if (copper    > 0) dropStack(new ItemStack(NyakoItems.COPPER_COIN,    copper));
        if (gold      > 0) dropStack(new ItemStack(NyakoItems.GOLD_COIN,      gold));
        if (emerald   > 0) dropStack(new ItemStack(NyakoItems.EMERALD_COIN,   emerald));
        if (diamond   > 0) dropStack(new ItemStack(NyakoItems.DIAMOND_COIN,   diamond));
        if (netherite > 0) dropStack(new ItemStack(NyakoItems.NETHERITE_COIN, netherite));
    }
}