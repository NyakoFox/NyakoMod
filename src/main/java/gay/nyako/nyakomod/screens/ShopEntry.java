package gay.nyako.nyakomod.screens;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public record ShopEntry (
        List<ItemStack> stacks,
        int price,
        Text name,
        Text description
) {}
