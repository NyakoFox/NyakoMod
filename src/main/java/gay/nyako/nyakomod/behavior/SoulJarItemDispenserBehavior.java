package gay.nyako.nyakomod.behavior;

import gay.nyako.nyakomod.item.SoulJarItem;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class SoulJarItemDispenserBehavior extends ItemDispenserBehavior {
    public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
        BlockPos pos = pointer.getPos();
        World world = pointer.getWorld();
        if (!stack.getOrCreateNbt().contains("entity")) {
            BlockPos blockPos = pointer.getPos().offset(direction);
            List<Entity> list = pointer.getWorld().getEntitiesByClass(
                    Entity.class,
                    new Box(blockPos), Entity::isAlive
            );

            Iterator<Entity> var5 = list.iterator();

            Entity entity;
            ItemStack newStack;
            do {
                if (!var5.hasNext()) {
                    return super.dispenseSilently(pointer, stack);
                }

                entity = var5.next();
                newStack = ((SoulJarItem) stack.getItem()).captureEntity(stack, null, (LivingEntity) entity);
            } while (newStack == null);

            return newStack;
        }
        ((SoulJarItem) stack.getItem()).spawnEntity(pos, direction, world, stack);
        return stack;
    }
}
