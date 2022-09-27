package gay.nyako.nyakomod;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.enums.Instrument;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;

public class InstrumentRegistry {
    public static final InstrumentRegister HARP = new InstrumentRegister("harp", SoundEvents.BLOCK_NOTE_BLOCK_HARP);
    public static final InstrumentRegister BASS = new InstrumentRegister("bass", SoundEvents.BLOCK_NOTE_BLOCK_HARP);

    public static void register() {
        HARP.register();
    }

    public static int adjustIndex(int i) {
        var negative = i < 0;

        var index = Math.abs(i) % 12;

        if (negative) {
            index *= -1;
        }

        return index;
    }

    public static InstrumentRegister fromBlockState(BlockState state) {
        if (state.isOf(Blocks.CLAY)) {
//            return FLUTE;
        } else if (state.isOf(Blocks.GOLD_BLOCK)) {
//            return BELL;
        } else if (state.isIn(BlockTags.WOOL)) {
//            return GUITAR;
        } else if (state.isOf(Blocks.PACKED_ICE)) {
//            return CHIME;
        } else if (state.isOf(Blocks.BONE_BLOCK)) {
//            return XYLOPHONE;
        } else if (state.isOf(Blocks.IRON_BLOCK)) {
//            return IRON_XYLOPHONE;
        } else if (state.isOf(Blocks.SOUL_SAND)) {
//            return COW_BELL;
        } else if (state.isOf(Blocks.PUMPKIN)) {
//            return DIDGERIDOO;
        } else if (state.isOf(Blocks.EMERALD_BLOCK)) {
//            return BIT;
        } else if (state.isOf(Blocks.HAY_BLOCK)) {
//            return BANJO;
        } else if (state.isOf(Blocks.GLOWSTONE)) {
//            return PLING;
        } else {
            Material material = state.getMaterial();
            if (material == Material.STONE) {
//                return BASEDRUM;
            } else if (material == Material.AGGREGATE) {
//                return SNARE;
            } else if (material == Material.GLASS) {
//                return HAT;
            } else {
                return material != Material.WOOD && material != Material.NETHER_WOOD ? HARP : BASS;
            }
        }

        return HARP;
    }
}
