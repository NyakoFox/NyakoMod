package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PistonBlockEntity.class)
public abstract class PistonBlockEntityMixin extends BlockEntity {

	public PistonBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Inject(method = "tick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/PistonBlockEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighbor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;)V", shift = At.Shift.AFTER))
	private static void injected(World world, BlockPos pos, BlockState state, PistonBlockEntity blockEntity, CallbackInfo ci) {
		if (blockEntity.getPushedBlock().getBlock().equals(Blocks.END_ROD)) {
			var movementDir = blockEntity.getMovementDirection();
			var newPos = pos.offset(movementDir);
			var newBlock = world.getBlockState(newPos).getBlock();
			if (newBlock.equals(Blocks.DIAMOND_ORE) || newBlock.equals(Blocks.DEEPSLATE_DIAMOND_ORE)) {
				world.setBlockState(newPos, Blocks.PACKED_ICE.getDefaultState());
				world.playSound(null, newPos.getX(), newPos.getY(), newPos.getZ(), NyakoSoundEvents.SAMSUNG, SoundCategory.BLOCKS, 1.0f, 1.0f);
			}
		}
	}
}
