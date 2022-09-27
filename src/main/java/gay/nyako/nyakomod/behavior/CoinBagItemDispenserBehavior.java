package gay.nyako.nyakomod.behavior;

import gay.nyako.nyakomod.NyakoItems;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class CoinBagItemDispenserBehavior extends ItemDispenserBehavior {

    public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
        Position pos = DispenserBlock.getOutputLocation(pointer);
        World world = pointer.getWorld();

        var nbt = stack.getOrCreateNbt();

        this.dispenseCoins(nbt, "copper", NyakoItems.COPPER_COIN_ITEM, direction, pos, world);
        this.dispenseCoins(nbt, "gold", NyakoItems.GOLD_COIN_ITEM, direction, pos, world);
        this.dispenseCoins(nbt, "emerald", NyakoItems.EMERALD_COIN_ITEM, direction, pos, world);
        this.dispenseCoins(nbt, "diamond", NyakoItems.DIAMOND_COIN_ITEM, direction, pos, world);
        this.dispenseCoins(nbt, "netherite", NyakoItems.NETHERITE_COIN_ITEM, direction, pos, world);

        stack.setNbt(nbt);

        return stack;
    }


    public void dispenseCoins(NbtCompound nbt, String tag, Item type, Direction direction, Position pos, World world) {
        if (nbt.contains(tag)) {
            int coins = nbt.getInt(tag);
            if (coins > 0) {
                var stack = new ItemStack(type);
                stack.setCount(coins);
                nbt.remove(tag);

                ItemDispenserBehavior.spawnItem(world, stack, 6, direction, pos);
            }
        }
    }
}
