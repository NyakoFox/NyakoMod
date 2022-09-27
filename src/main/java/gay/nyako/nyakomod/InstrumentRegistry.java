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
    public static final InstrumentRegister BASS = new InstrumentRegister("bass", SoundEvents.BLOCK_NOTE_BLOCK_BASS);
    public static final InstrumentRegister FLUTE = new InstrumentRegister("flute", SoundEvents.BLOCK_NOTE_BLOCK_FLUTE);
    public static final InstrumentRegister BELL = new InstrumentRegister("bell", SoundEvents.BLOCK_NOTE_BLOCK_BELL);
    public static final InstrumentRegister GUITAR = new InstrumentRegister("guitar", SoundEvents.BLOCK_NOTE_BLOCK_GUITAR);
    public static final InstrumentRegister CHIME = new InstrumentRegister("chime", SoundEvents.BLOCK_NOTE_BLOCK_CHIME);
    public static final InstrumentRegister XYLOPHONE = new InstrumentRegister("xylophone", SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE);
    public static final InstrumentRegister IRON_XYLOPHONE = new InstrumentRegister("iron_xylophone", SoundEvents.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE);
    public static final InstrumentRegister COW_BELL = new InstrumentRegister("cow_bell", SoundEvents.BLOCK_NOTE_BLOCK_COW_BELL);
    public static final InstrumentRegister DIDGERIDOO = new InstrumentRegister("didgeridoo", SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO);
    public static final InstrumentRegister BIT = new InstrumentRegister("bit", SoundEvents.BLOCK_NOTE_BLOCK_BIT);
    public static final InstrumentRegister BANJO = new InstrumentRegister("banjo", SoundEvents.BLOCK_NOTE_BLOCK_BANJO);
    public static final InstrumentRegister PLING = new InstrumentRegister("pling", SoundEvents.BLOCK_NOTE_BLOCK_PLING);
    public static final InstrumentRegister BASEDRUM = new InstrumentRegister("bd", SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM);
    public static final InstrumentRegister SNARE = new InstrumentRegister("snare", SoundEvents.BLOCK_NOTE_BLOCK_SNARE);
    public static final InstrumentRegister HAT = new InstrumentRegister("hat", SoundEvents.BLOCK_NOTE_BLOCK_HAT);

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
            return FLUTE;
        } else if (state.isOf(Blocks.GOLD_BLOCK)) {
            return BELL;
        } else if (state.isIn(BlockTags.WOOL)) {
            return GUITAR;
        } else if (state.isOf(Blocks.PACKED_ICE)) {
            return CHIME;
        } else if (state.isOf(Blocks.BONE_BLOCK)) {
            return XYLOPHONE;
        } else if (state.isOf(Blocks.IRON_BLOCK)) {
            return IRON_XYLOPHONE;
        } else if (state.isOf(Blocks.SOUL_SAND)) {
            return COW_BELL;
        } else if (state.isOf(Blocks.PUMPKIN)) {
            return DIDGERIDOO;
        } else if (state.isOf(Blocks.EMERALD_BLOCK)) {
            return BIT;
        } else if (state.isOf(Blocks.HAY_BLOCK)) {
            return BANJO;
        } else if (state.isOf(Blocks.GLOWSTONE)) {
            return PLING;
        } else {
            Material material = state.getMaterial();
            if (material == Material.STONE) {
                return BASEDRUM;
            } else if (material == Material.AGGREGATE) {
                return SNARE;
            } else if (material == Material.GLASS) {
                return HAT;
            } else {
                return material != Material.WOOD && material != Material.NETHER_WOOD ? HARP : BASS;
            }
        }
    }
}
