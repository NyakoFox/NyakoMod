package gay.nyako.nyakomod.item;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class NetherPortalBucketItem extends Item {

    public NetherPortalBucketItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        if (user instanceof PlayerEntity && !((PlayerEntity)user).getAbilities().creativeMode) {
            stack.decrement(1);
        }
        if (!world.isClient) {
            MinecraftServer minecraftServer = world.getServer();
            if (world.getRegistryKey() == World.END) {
                world.createExplosion(null, user.getX(), user.getY(), user.getZ(), 5.0f, Explosion.DestructionType.BREAK);
                if (stack.isEmpty()) {
                    return new ItemStack(Items.BUCKET);
                }
                return stack;
            }
            ServerWorld serverWorld2 = minecraftServer.getWorld(world.getRegistryKey() == World.NETHER ? World.OVERWORLD : World.NETHER);
            user.dismountVehicle();
            if (serverWorld2 != null && minecraftServer.isNetherAllowed()) {
                world.getProfiler().push("portal");
                user.lastNetherPortalPosition = user.getBlockPos().toImmutable();
                user.resetPortalCooldown();
                user.moveToWorld(serverWorld2);
                world.getProfiler().pop();
            }
        }
        if (stack.isEmpty()) {
            return new ItemStack(Items.BUCKET);
        }
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}
