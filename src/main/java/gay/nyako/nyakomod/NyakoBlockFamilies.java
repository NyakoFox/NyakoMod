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

    public static final BlockFamily BENTHIC = BlockFamilies
            .register(NyakoBlocks.BENTHIC_PLANKS)
            .button(NyakoBlocks.BENTHIC_BUTTON)
            .fence(NyakoBlocks.BENTHIC_FENCE)
            .fenceGate(NyakoBlocks.BENTHIC_FENCE_GATE)
            .pressurePlate(NyakoBlocks.BENTHIC_PRESSURE_PLATE)
            .sign(NyakoBlocks.BENTHIC_SIGN, NyakoBlocks.BENTHIC_WALL_SIGN)
            .slab(NyakoBlocks.BENTHIC_SLAB)
            .stairs(NyakoBlocks.BENTHIC_STAIRS)
            .door(NyakoBlocks.BENTHIC_DOOR)
            .trapdoor(NyakoBlocks.BENTHIC_TRAPDOOR)
            .group("wooden")
            .unlockCriterionName("has_planks")
            .build();
}
