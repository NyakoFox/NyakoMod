package gay.nyako.nyakomod.mixin;

import com.google.common.collect.Lists;
import gay.nyako.nyakomod.NyakoItems;
import gay.nyako.nyakomod.entity.NetherPortalProjectileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.function.Predicate;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin extends RangedWeaponItem {

    public CrossbowItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "getHeldProjectiles()Ljava/util/function/Predicate;", at = @At("RETURN"), cancellable = true)
    private void injected(CallbackInfoReturnable<Predicate<ItemStack>> cir) {
        cir.setReturnValue(cir.getReturnValue().or(stack -> stack.isOf(NyakoItems.NETHER_PORTAL_STRUCTURE)));
    }

    @Redirect(method = "shoot(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;FZFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private static boolean injected(ItemStack instance, Item item) {
        return instance.isOf(Items.FIREWORK_ROCKET) || instance.isOf(NyakoItems.NETHER_PORTAL_STRUCTURE);
    }

    @ModifyVariable(method = "shoot(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;FZFFF)V", at = @At("STORE"), ordinal = 0)
    private static ProjectileEntity injected(ProjectileEntity projectile, World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectileStack) {
        if (projectileStack.isOf(NyakoItems.NETHER_PORTAL_STRUCTURE)) {

            NbtCompound explosionCompound = new NbtCompound();

            explosionCompound.putIntArray("Colors", new int[] {2651799, 6719955});
            explosionCompound.putByte("Type", (byte)FireworkRocketItem.Type.BURST.getId());

            NbtCompound nbtCompound2 = projectileStack.getOrCreateSubNbt("Fireworks");
            NbtList nbtList = new NbtList();

            nbtList.add(explosionCompound);

            nbtCompound2.putByte("Flight", (byte)3);
            nbtCompound2.put("Explosions", nbtList);
            projectile = new NetherPortalProjectileEntity(world, projectileStack, shooter, shooter.getX(), shooter.getEyeY() - (double)0.15f, shooter.getZ());
        }
        return projectile;
    }
}