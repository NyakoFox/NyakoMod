package gay.nyako.nyakomod;

import net.minecraft.block.BlockState;
import net.minecraft.sound.SoundEvents;

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
        BASS.register();
        FLUTE.register();
        BELL.register();
        GUITAR.register();
        CHIME.register();
        XYLOPHONE.register();
        IRON_XYLOPHONE.register();
        COW_BELL.register();
        DIDGERIDOO.register();
        BIT.register();
        BANJO.register();
        PLING.register();
        BASEDRUM.register();
        SNARE.register();
        HAT.register();
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
        return switch(state.getInstrument())
        {
            case HARP, ZOMBIE, SKELETON, CREEPER, DRAGON, WITHER_SKELETON, PIGLIN, CUSTOM_HEAD: yield HARP;
            case BASEDRUM: yield BASEDRUM;
            case SNARE: yield SNARE;
            case HAT: yield HAT;
            case BASS: yield BASS;
            case FLUTE: yield FLUTE;
            case BELL: yield BELL;
            case GUITAR: yield GUITAR;
            case CHIME: yield CHIME;
            case XYLOPHONE: yield XYLOPHONE;
            case IRON_XYLOPHONE: yield IRON_XYLOPHONE;
            case COW_BELL: yield COW_BELL;
            case DIDGERIDOO: yield DIDGERIDOO;
            case BIT: yield BIT;
            case BANJO: yield BANJO;
            case PLING: yield PLING;
        };
    }
}
