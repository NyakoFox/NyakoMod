package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LightningEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningEntity.class)
public class LightningEntityMixin {
    @Inject(method = "cleanOxidation(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V", at = @At("HEAD"))
    private static void cleanOxidation(World world, BlockPos pos, CallbackInfo ci) {
        if (world.getBlockState(pos).getBlock() == Blocks.IRON_BLOCK)
        {
            world.setBlockState(pos, NyakoBlocks.CHARGED_IRON_BLOCK.getDefaultState());
        }
    }
}
