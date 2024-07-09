package gay.nyako.nyakomod.block.custom;

import gay.nyako.nyakomod.NyakoEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HangingSignBlockEntity;
import net.minecraft.util.math.BlockPos;

public class CustomHangingSignBlockEntity extends HangingSignBlockEntity {
    public CustomHangingSignBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return NyakoEntities.CUSTOM_HANGING_SIGN_BLOCK_ENTITY;
    }
}
