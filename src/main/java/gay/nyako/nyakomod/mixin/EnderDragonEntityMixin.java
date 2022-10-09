package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.utils.CunkCoinUtils;
import gay.nyako.nyakomod.NyakoItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin extends MobEntity {
    @Shadow @Final @Nullable private EnderDragonFight fight;

    protected EnderDragonEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injected(CallbackInfo ci) {
        this.setCustomName(Text.literal("Jender Jragon"));
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

        if (copper    > 0) dropStack(new ItemStack(NyakoItems.COPPER_COIN_ITEM,    copper));
        if (gold      > 0) dropStack(new ItemStack(NyakoItems.GOLD_COIN_ITEM,      gold));
        if (emerald   > 0) dropStack(new ItemStack(NyakoItems.EMERALD_COIN_ITEM,   emerald));
        if (diamond   > 0) dropStack(new ItemStack(NyakoItems.DIAMOND_COIN_ITEM,   diamond));
        if (netherite > 0) dropStack(new ItemStack(NyakoItems.NETHERITE_COIN_ITEM, netherite));
    }
}