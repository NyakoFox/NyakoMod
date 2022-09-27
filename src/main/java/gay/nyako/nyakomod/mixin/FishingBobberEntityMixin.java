package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoItems;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin extends ProjectileEntity {

    public FishingBobberEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract PlayerEntity getPlayerOwner();

    @Inject(method = "use(Lnet/minecraft/item/ItemStack;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private void injected(ItemStack usedItem, CallbackInfoReturnable<Integer> cir) {
        int rand = world.random.nextBetween(0, 100);
        if (rand < 1) {
            spawnItem(new ItemStack(NyakoItems.GOLD_COIN_ITEM, world.random.nextBetween(1, 2)));
        } else if (rand < 50) {
            spawnItem(new ItemStack(NyakoItems.COPPER_COIN_ITEM, world.random.nextBetween(1, 16)));
        }

        // fishing up tnt
        /*if (rand <= 1) {
            PlayerEntity playerEntity = getPlayerOwner();
            TntEntity tntEntity = new TntEntity(world, getX(), getY(), getZ(), playerEntity);
            tntEntity.setFuse(16);
            double d = playerEntity.getX() - this.getX();
            double e = playerEntity.getY() - this.getY();
            double f = playerEntity.getZ() - this.getZ();
            tntEntity.setVelocity(d * 0.1, e * 0.1 + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08, f * 0.1);
            this.world.spawnEntity(tntEntity);
        }*/
    }

    public void spawnItem(ItemStack itemStack) {
        PlayerEntity playerEntity = getPlayerOwner();
        ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), itemStack);
        double d = playerEntity.getX() - this.getX();
        double e = playerEntity.getY() - this.getY();
        double f = playerEntity.getZ() - this.getZ();
        itemEntity.setVelocity(d * 0.1, e * 0.1 + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08, f * 0.1);
        this.world.spawnEntity(itemEntity);
    }
}
