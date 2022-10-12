package gay.nyako.nyakomod.access;

import net.minecraft.block.BlockState;

public interface TntEntityAccess {
    void setCopyBlockState(BlockState state);
    BlockState getCopyBlockState();
}
