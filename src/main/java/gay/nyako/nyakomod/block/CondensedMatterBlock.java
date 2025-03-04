package gay.nyako.nyakomod.block;

import gay.nyako.nyakomod.data.CondensedMatterOutcomes;
import gay.nyako.nyakomod.outcomes.OutcomeContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CondensedMatterBlock extends Block {
    public CondensedMatterBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        if (player != null && !world.isClient())
        {
            var outcome = CondensedMatterOutcomes.random(world.getRandom());
            outcome.apply(new OutcomeContext((ServerPlayerEntity) player, (ServerWorld) world, pos));
        }
    }
}
