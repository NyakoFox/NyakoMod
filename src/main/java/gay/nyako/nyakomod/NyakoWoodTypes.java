package gay.nyako.nyakomod;

import net.minecraft.block.WoodType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;

public class NyakoWoodTypes {
    public static final WoodType ECHO = WoodType.register(new WoodType("echo", NyakoBlockSetTypes.ECHO, BlockSoundGroup.NETHER_WOOD, BlockSoundGroup.NETHER_WOOD_HANGING_SIGN, SoundEvents.BLOCK_NETHER_WOOD_FENCE_GATE_CLOSE, SoundEvents.BLOCK_NETHER_WOOD_FENCE_GATE_OPEN));
}
