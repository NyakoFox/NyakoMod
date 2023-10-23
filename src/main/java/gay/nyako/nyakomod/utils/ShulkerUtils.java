package gay.nyako.nyakomod.utils;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.tag.BlockTags;

public class ShulkerUtils {
    public static boolean IsShulkerBox(ItemStack stack) {
        var block = Block.getBlockFromItem(stack.getItem());
        return block.getDefaultState().isIn(BlockTags.SHULKER_BOXES);
    }
}

