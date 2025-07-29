package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.NyakoSoundEvents;
import gay.nyako.nyakomod.entity.BombEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BombItem extends Item {
    public BombItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                NyakoSoundEvents.ENTITY_BOMB_THROW,
                SoundCategory.NEUTRAL,
                0.5F,
                0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!world.isClient) {
            BombEntity bombEntity = getBombEntity(world, user);
            bombEntity.setItem(itemStack);
            bombEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 0.5F, 1.0F);
            world.spawnEntity(bombEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    protected BombEntity getBombEntity(World world, PlayerEntity user) {
        return new BombEntity(world, user);
    }
}
