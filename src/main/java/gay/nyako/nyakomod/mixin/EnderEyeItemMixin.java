package gay.nyako.nyakomod.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderEyeItem.class)
public class EnderEyeItemMixin extends Item {
    public EnderEyeItemMixin(Settings settings) {
        super(settings);
    }

    @Redirect(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V"), require = 0)
    private void syncWorldEvent(World instance, int eventID, BlockPos blockPos, int data) {
        int filledFrames = -1;
        // Check around in a 9x9 area centered around this block
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                BlockState state = instance.getBlockState(blockPos.add(x, 0, z));
                if (state.getBlock() instanceof EndPortalFrameBlock) {
                    if (state.get(EndPortalFrameBlock.EYE)) {
                        filledFrames++;
                    }
                }
            }
        }
        instance.syncWorldEvent(eventID, blockPos, filledFrames);
    }
}
