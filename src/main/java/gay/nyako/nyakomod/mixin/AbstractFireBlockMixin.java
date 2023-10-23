package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoEntities;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.entity.HerobrineEntity;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFireBlock.class)
public class AbstractFireBlockMixin {

    @Inject(method = "onBlockAdded(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)V", at = @At("TAIL"))
    private void injected(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        if (world.getBlockState(pos.down()).getBlock() != Blocks.NETHERRACK) return;
        // Check flat 3x3 plane one block below the netherrack
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <=1; z++) {
                if (world.getBlockState(pos.add(x, -2, z)).getBlock() != Blocks.GOLD_BLOCK) return;
            }
        }
        // Check for redstone torches all around the netherrack
        if (world.getBlockState(pos.down().north()).getBlock() != Blocks.REDSTONE_TORCH) return;
        if (world.getBlockState(pos.down().south()).getBlock() != Blocks.REDSTONE_TORCH) return;
        if (world.getBlockState(pos.down().east()).getBlock() != Blocks.REDSTONE_TORCH) return;
        if (world.getBlockState(pos.down().west()).getBlock() != Blocks.REDSTONE_TORCH) return;

        var lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
        lightning.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        lightning.setCosmetic(true);
        world.spawnEntity(lightning);

        var entity = new HerobrineEntity(NyakoEntities.HEROBRINE, world);
        entity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        world.spawnEntity(entity);
    }
}
