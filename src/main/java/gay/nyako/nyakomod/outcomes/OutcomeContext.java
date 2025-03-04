package gay.nyako.nyakomod.outcomes;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public record OutcomeContext(ServerPlayerEntity player, ServerWorld world, BlockPos blockPos) {

}
