package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LightningEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LightningEntity.class)
public class LightningEntityMixin {
    @Inject(method = "cleanOxidation(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V", at = @At("HEAD"))
    private static void cleanOxidation(World world, BlockPos pos, CallbackInfo ci) {
        if (world.getBlockState(pos).getBlock() == Blocks.IRON_BLOCK)
        {
            world.setBlockState(pos, NyakoBlocks.CHARGED_IRON_BLOCK.getDefaultState());
        }
    }

    @Inject(method = "cleanOxidationAround(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Ljava/util/Optional;", at = @At(value = "HEAD"), cancellable = true)
    private static void cleanOxidationAround(World world, BlockPos pos, CallbackInfoReturnable<Optional<BlockPos>> cir) {
        for (BlockPos blockPos : BlockPos.iterateRandomly(world.random, 10, pos, 1)) {
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.getBlock() == Blocks.IRON_BLOCK) {
                world.setBlockState(blockPos, NyakoBlocks.CHARGED_IRON_BLOCK.getDefaultState());
                world.syncWorldEvent(WorldEvents.ELECTRICITY_SPARKS, blockPos, -1);
                cir.setReturnValue(Optional.of(blockPos));
            }
        }
    }
}
