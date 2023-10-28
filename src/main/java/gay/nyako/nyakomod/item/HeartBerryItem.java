package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.NyakoItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class HeartBerryItem extends Item {
    public HeartBerryItem(FabricItemSettings settings) {
        super(settings);
    }

    public int getMaxUseTime(ItemStack stack) {
        return 8;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        user.heal(2);
        if (user instanceof PlayerEntity playerEntity) {
            playerEntity.getItemCooldownManager().set(this, 5);
        }
        return user.eatFood(world, stack);
    }
}
