package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.entity.MonitorEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TestItem extends Item {
    public TestItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {

        var distance = 128;

        Vec3d pos = player.getCameraPosVec(0.0F);
        Vec3d ray = pos.add(player.getRotationVector().multiply(distance));

        EntityHitResult entityHitResult = net.minecraft.entity.projectile.ProjectileUtil.getEntityCollision(player.world, player, pos, ray, player.getBoundingBox().expand(distance), entity -> true);

        if (entityHitResult != null && entityHitResult.getType() == HitResult.Type.ENTITY) {
            Entity entity = entityHitResult.getEntity();
            if (entity instanceof MonitorEntity monitorEntity) {

                System.out.println("%s: %dx%d | Deco: %s, Block: %s".formatted(
                        world.isClient() ? "client" : "server",
                        monitorEntity.getMonitorWidth(),
                        monitorEntity.getMonitorHeight(),
                        monitorEntity.getDecorationBlockPos().toShortString(),
                        monitorEntity.getBlockPos().toShortString()
                ));
            }
        }
        return TypedActionResult.success(player.getStackInHand(hand));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
