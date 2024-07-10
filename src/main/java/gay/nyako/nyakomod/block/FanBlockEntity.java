package gay.nyako.nyakomod.block;

import gay.nyako.nyakomod.NyakoEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

public class FanBlockEntity extends BlockEntity {
    public FanBlockEntity(BlockPos pos, BlockState state) {
        super(NyakoEntities.FAN_BLOCK_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, FanBlockEntity fanBlockEntity) {
        var direction = blockState.get(Properties.FACING);

        var pushX = direction.getOffsetX();
        var pushY = direction.getOffsetY();
        var pushZ = direction.getOffsetZ();

        int distance = 5;
        double force = 0.15;

        world.getOtherEntities(null, new Box(blockPos).offset(pushX, pushY, pushZ).stretch(pushX * distance - 1, pushY * distance - 1, pushZ * distance - 1)).forEach(entity -> {
            var falloff = 1 - Math.min(1, Math.sqrt(entity.getPos().squaredDistanceTo(blockPos.toCenterPos())) / distance);
            entity.addVelocity(pushX * force * falloff, pushY * force * falloff, pushZ * force * falloff);
        });
    }
}
