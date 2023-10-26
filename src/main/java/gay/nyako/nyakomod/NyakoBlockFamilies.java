package gay.nyako.nyakomod;

import net.minecraft.block.Blocks;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;

public class NyakoBlockFamilies {
    public static final BlockFamily ECHO = BlockFamilies
            .register(NyakoBlocks.ECHO_PLANKS)
            .button(NyakoBlocks.ECHO_BUTTON)
            .fence(NyakoBlocks.ECHO_FENCE)
            .fenceGate(NyakoBlocks.ECHO_FENCE_GATE)
            .pressurePlate(NyakoBlocks.ECHO_PRESSURE_PLATE)
            .sign(NyakoBlocks.ECHO_SIGN, NyakoBlocks.ECHO_WALL_SIGN)
            .slab(NyakoBlocks.ECHO_SLAB)
            .stairs(NyakoBlocks.ECHO_STAIRS)
            .door(NyakoBlocks.ECHO_DOOR)
            .trapdoor(NyakoBlocks.ECHO_TRAPDOOR)
            .group("wooden")
            .unlockCriterionName("has_planks")
            .build();
}
