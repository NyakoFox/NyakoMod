package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.access.PlayerEntityAccess;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
public abstract class MilkBucketItemMixin extends Item {

    public MilkBucketItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "finishUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;clearStatusEffects()Z"))
    private void injected(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (user instanceof PlayerEntity player) {
            var access = (PlayerEntityAccess) user;
            access.addMilk(5);
            access.addMilkSaturation(1);

            var nbt = stack.getOrCreateNbt();
            if (nbt.getBoolean("isFromPlayer") && !world.isClient()) {
                player.getScoreboard().forEachScore(NyakoMod.TIMES_MILKED_CRITERIA, player.getEntityName(), score -> score.setScore(score.getScore() + 1));
            }
        }
    }
}
