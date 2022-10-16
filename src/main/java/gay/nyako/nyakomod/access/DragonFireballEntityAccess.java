package gay.nyako.nyakomod.access;

import net.minecraft.block.BlockState;

public interface DragonFireballEntityAccess {
    void setFromAttack(boolean fromAttack);
    boolean isFromAttack();
}
