package gay.nyako.nyakomod;

import F;
import I;
import J;
import gay.nyako.nyakomod.block.NoteBlockPlusBlockEntity;
import java.util.List;
import me.stupidcat.abcparser.ABCParser;
import me.stupidcat.abcparser.ABCSong;
import me.stupidcat.abcparser.struct.SongChord;
import me.stupidcat.abcparser.struct.SongComponent;
import me.stupidcat.abcparser.struct.SongNote;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;

public class SongPlayer {
    NoteBlockPlusBlockEntity blockEntity;

    ABCSong song;
    SongComponent[] songComponents;
    boolean playing = false;
    long initial = 0;
    int pointer = 0;
    float progress = 0;
    int previousSongProgress = 0;

    double songLength;

    public SongPlayer(NoteBlockPlusBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    public void play(String input) {
        playing = !playing;
        initial = System.currentTimeMillis();
        progress = 0;

        if (playing) {

            song = new ABCSong()
                    .setBpm(160 * 4)
                    .setMeter("4/4")
                    .setDefaultNoteLength("1/4")
                    .setKey("C")
            ;

            var parser = new ABCParser(song);

//            var components = parser.parse(megalovania);
            try {
                var components = parser.parse(input);
//             var components = parser.parse("G,, A,, B,, C, D, E, F, G, A, B, C D E F G A B c d e f g a b c' d' e' f' g' a' b' c'' d'' e'' f'' g'' a'' b'' c''' d''' e''' f'''");
//             var components = parser.parse("""
//                e ^f ^g a b ^c' ^d' e'
//             """);

                previousSongProgress = 0;

                songComponents = new SongComponent[components.size()];

                components.toArray(songComponents);
                songLength = songComponents[songComponents.length - 1].getEnd();
                pointer = 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getSongProgress() {
        var progress = System.currentTimeMillis() - initial;
        return (int)Math.ceil(progress / songLength * 15d);
    }

    public void tick() {
        if (!playing) {
            return;
        }

        var ms = System.currentTimeMillis() - initial;

        var progress = getSongProgress();
        if (progress != previousSongProgress) {
            previousSongProgress = progress;
            blockEntity.markDirty();
        }

        while (pointer < songComponents.length && ms > songComponents[pointer].getStart()) {
            var comp = songComponents[pointer];
            if (comp instanceof SongChord chord) {
                for (var note :  chord.notes) {
                    playNote(note);
                }
            } else if (comp instanceof SongNote note) {
                playNote(note);
            }
            pointer++;
        }

        if (pointer >= songComponents.length) {
            playing = false;
            blockEntity.markDirty();
        }
    }

    public void playNote(SongNote note) {
        var noteIndex = note.getNote() - 12 + 6;
        var pitch = noteToPitch(note);
        var instrument = InstrumentRegistry.fromBlockState(blockEntity.getWorld().getBlockState(blockEntity.getPos().down()));

        var sound = instrument.getFromIndex(noteIndex);

        if (sound == null) {
            NyakoMod.LOGGER.warn("Tried playing invalid note " + note.getRawNote());
            return;
        }

        if (canPlayNote(pitch)) {
            if (!note.isRest()) {
                if (blockEntity != null) {
                    playSound(sound, pitch);
                }
            }
        } else {
            // client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_SNARE, 1, 1);
        }
    }

    public void playSound(SoundEvent sound, float pitch) {
        if (blockEntity != null) {
            var pos = blockEntity.getPos();
            ServerWorld world = (ServerWorld)blockEntity.getWorld();
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), sound, SoundCategory.RECORDS, 3, pitch);
        }
    }

    public boolean canPlayNote(float pitch) {
        return pitch >= 0.5 && pitch <= 2.0;
    }

    public float noteToPitch(SongNote note) {
        /*
        float step = 1f / 12f;
        float noteStep = note.getNote() * step;

        return (noteStep + step * 5) * 0.5f;
         */
        var noteIndex = note.getNote() - 12 + 6;
        var newNoteIndex = InstrumentRegistry.adjustIndex(noteIndex);

        float f = (float)Math.pow(2.0D, (double)(newNoteIndex) / 12.0D);
        // System.out.println(noteIndex);

        return f;
    }

    public boolean isPlaying() {
        return playing;
    }
}
