package gay.nyako.nyakomod.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerInventory.class)
public interface PlayerInventoryInvoker {
  @Invoker int callAddStack(ItemStack stack);
}
