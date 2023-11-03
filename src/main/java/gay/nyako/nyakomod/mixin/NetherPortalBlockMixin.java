package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin extends Block {

	public NetherPortalBlockMixin(Settings settings) {
		super(settings);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction.Axis finalAxis = Direction.Axis.X;
		switch (ctx.getHorizontalPlayerFacing().getAxis()) {
			case X: finalAxis = Direction.Axis.Z; break;
			case Z: finalAxis = Direction.Axis.X; break;
		}
		return this.getDefaultState().with(Properties.HORIZONTAL_AXIS, finalAxis);
	}

	@Inject(method = "onEntityCollision(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V", at = @At(value = "HEAD"), cancellable = true)
	private void injected(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
		if (entity instanceof ItemEntity itemEntity) {
			var stack = itemEntity.getStack();
			if (stack.isOf(Items.BUCKET)) {
				if (!entity.getWorld().isClient()) {
					world.setBlockState(pos, Blocks.AIR.getDefaultState());

					var netherPortalStack = new ItemStack(NyakoItems.NETHER_PORTAL_BUCKET);
					entity.getWorld().playSound(null, itemEntity.getBlockPos(), SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
					itemEntity.setStack(netherPortalStack);

					ci.cancel();
				}
			}
		}
	}
}
