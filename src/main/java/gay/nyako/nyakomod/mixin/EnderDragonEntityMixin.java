package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.CunkCoinUtils;
import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin extends LivingEntity {

    protected EnderDragonEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method= "updatePostDeath()V", at=@At(value="INVOKE", target="Lnet/minecraft/entity/ExperienceOrbEntity;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;I)V"))
    public void redirect(ServerWorld world, Vec3d pos, int amount) {
        ExperienceOrbEntity.spawn(world, pos, amount);
        Map<CunkCoinUtils.CoinValue, Integer> map = CunkCoinUtils.valueToSplit(MathHelper.ceil(amount * 0.5));

        Integer copper = map.get(CunkCoinUtils.CoinValue.COPPER);
        Integer gold = map.get(CunkCoinUtils.CoinValue.GOLD);
        Integer emerald = map.get(CunkCoinUtils.CoinValue.EMERALD);
        Integer diamond = map.get(CunkCoinUtils.CoinValue.DIAMOND);
        Integer netherite = map.get(CunkCoinUtils.CoinValue.NETHERITE);

        if (copper > 0) {
            ItemStack itemStack = new ItemStack(NyakoMod.COPPER_COIN_ITEM);
            itemStack.setCount(copper);
            dropStack(itemStack);
        }

        if (gold > 0) {
            ItemStack itemStack = new ItemStack(NyakoMod.GOLD_COIN_ITEM);
            itemStack.setCount(gold);
            dropStack(itemStack);
        }

        if (emerald > 0) {
            ItemStack itemStack = new ItemStack(NyakoMod.EMERALD_COIN_ITEM);
            itemStack.setCount(emerald);
            dropStack(itemStack);
        }

        if (diamond > 0) {
            ItemStack itemStack = new ItemStack(NyakoMod.DIAMOND_COIN_ITEM);
            itemStack.setCount(diamond);
            dropStack(itemStack);
        }

        if (netherite > 0) {
            ItemStack itemStack = new ItemStack(NyakoMod.NETHERITE_COIN_ITEM);
            itemStack.setCount(netherite);
            dropStack(itemStack);
        }

        return;
    }
}