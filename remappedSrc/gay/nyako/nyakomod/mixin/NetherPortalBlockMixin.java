package gay.nyako.nyakomod.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

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
}
