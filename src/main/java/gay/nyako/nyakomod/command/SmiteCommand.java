package gay.nyako.nyakomod.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public final class SmiteCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("smite")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayer();
                    if (player == null) {
                        return 0;
                    }

                    var smiteDistance = 128;

                    Vec3d pos = player.getCameraPosVec(0.0F);
                    Vec3d ray = pos.add(player.getRotationVector().multiply(smiteDistance));

                    EntityHitResult entityHitResult = net.minecraft.entity.projectile.ProjectileUtil.getEntityCollision(player.getWorld(), player, pos, ray, player.getBoundingBox().expand(smiteDistance), entity -> true);

                    if (entityHitResult != null && entityHitResult.getType() == HitResult.Type.ENTITY) {
                        Entity entity = entityHitResult.getEntity();
                        smiteEntity(entity);
                        return 1;
                    }

                    var result = player.raycast(smiteDistance, 0, false);
                    if (result.getType() == HitResult.Type.BLOCK) {
                        BlockPos blockPos = ((BlockHitResult) result).getBlockPos();
                        var lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, player.getWorld());
                        lightning.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                        player.getWorld().spawnEntity(lightning);
                    }
                    return 1;
                })
                .then(CommandManager.argument("target", EntityArgumentType.entities())
                        .executes(context -> {
                            var entities = EntityArgumentType.getEntities(context, "target");
                            for (var entity : entities) {
                                smiteEntity(entity);
                            }
                            return 1;
                        })
                )
        );
    }

    private static void smiteEntity(Entity entity) {
        entity.damage(entity.getDamageSources().lightningBolt(), 5);
        var lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, entity.getWorld());
        lightning.setPosition(entity.getPos());
        entity.getWorld().spawnEntity(lightning);
        lightning.setCosmetic(true);
    }
}
