package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoItems;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.ItemConvertible;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ComposterBlock.class)
// TODO: figure out why this doesnt work (not included in mixins file for now)
public class ComposterBlockMixin {
    @Shadow
    private static void registerCompostableItem(float levelIncreaseChance, ItemConvertible item) {}

    @Inject(method = "registerDefaultCompostableItems", at = @At("TAIL"))
    private static void registerDefaultCompostableItems(CallbackInfo info)
    {
        registerCompostableItem(0.65f, NyakoItems.GREEN_APPLE);
    }
}
