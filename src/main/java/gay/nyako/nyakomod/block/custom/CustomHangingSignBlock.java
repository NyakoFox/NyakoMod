package gay.nyako.nyakomod.block.custom;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class CustomHangingSignBlock extends HangingSignBlock {
    public CustomHangingSignBlock(Settings settings, WoodType woodType) {
        super(settings, woodType);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CustomHangingSignBlockEntity(pos, state);
    }
}
