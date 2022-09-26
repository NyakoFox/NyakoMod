package gay.nyako.nyakomod.test;

import me.stupidcat.abcparser.ABCParser;
import me.stupidcat.abcparser.ABCSong;
import me.stupidcat.abcparser.struct.SongChord;
import me.stupidcat.abcparser.struct.SongComponent;
import me.stupidcat.abcparser.struct.SongNote;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.List;

public class SongPlayer {
    MinecraftClient client;

    public static String input = """
z2 [G,11/24E11/24E,11/24G,11/24] [z/24E11/24] [A,11/24^F11/24E,11/24A,11/24] [z/24F11/24] [E11/24G5/4B,4/3B,4/3] [z/24G5/4] E,11/24 z/24\s
D11/24 z/24 [C11/24A11/24E,11/24C11/24] [z/24A11/24] [B,11/24G11/24E11/24B,11/24] [z/24G11/24] [A,11/24F11/24E,11/24A,11/24] [z/24F11/24] [G,11/24E11/24C11/24G,11/24] [z/24E11/24] [^F,11/24D11/24G,11/24F,11/24] [z/24D11/24] [G,11/24E11/24C11/24G,11/24] [z/24E11/24] [G,11/24C9/4E,7/3E,7/3] [z/24C9/4]\s
C11/24 z/24 c11/24 z/24 B11/24 z/24 c11/24 z/24 [A,11/24C11/6A11/6C11/6] [z/24A11/6] A,11/24 z/24 A11/24 z/24 A,11/24 z/24\s
[G,11/24D17/24B17/24D17/24] [z/24B17/24] [z/4G,11/24] [z/4C17/24A17/24C17/24] [z5/24G11/24] [z7/24A17/24] [B,11/24G11/24G,11/24B,11/24] [z/24G11/24] [=F,11/24=F15/4A,23/6A,23/6] [z/24F15/4] F,11/24 z/24 F11/24 z/24 F,11/24 z/24\s
E11/24 z/24 F,11/24 z/24 F11/24 z/24 F,11/24 z/24 [E/3E,11/24G,4/3G,4/3] z/8 [z/24E/3] [C/3E,11/24] z/8 [z/24C/3] [G/3E,11/24] z/8 [z/24G/3] [E/3E,11/24C,4/3C,4/3] z/8 [z/24E/3]\s
[c/3E11/24] z/8 [z/24c/3] [G/3E,11/24] z/8 [z/24G/3] [E/3E,11/24G,23/24G,23/24] z/8 [z/24E/3] [C/3E,11/24] z/8 [z/24C/3] [^F/3F,11/24A,23/6A,23/6] z/8 [z/24F/3] [D/3F,11/24] z/8 [z/24D/3] [A/3F,11/24] z/8 [z/24A/3] [F/3F,11/24] z/8 [z/24F/3]\s
[d/3=F11/24] z/8 [z/24d/3] [A/3F,11/24] z/8 [z/24A/3] [^F/3F,11/24] z/8 [z/24F/3] [D/3F,11/24] z/8 [z/24D/3] [F/3^F,11/24A,4/3A,4/3] z/8 [z/24F/3] [D/3F,11/24] z/8 [z/24D/3] [A/3F,11/24] z/8 [z/24A/3] [F/3F,11/24D,4/3D,4/3] z/8 [z/24F/3]\s
[d/3F11/24] z/8 [z/24d/3] [A/3D11/24] z/8 [z/24A/3] [F/3F11/24A,23/24A,23/24] z/8 [z/24F/3] [D/3F,11/24] z/8 [z/24D/3] [G/3G,11/24B,11/6B,11/6] z/8 [z/24G/3] [D/3G,11/24] z/8 [z/24D/3] [B/3G11/24] z/8 [z/24B/3] [D/3G,11/24] z/8 [z/24D/3]\s
[e/3^G,11/24B,17/24B,17/24] z/8 [z/24e/3] [z/4B/3E11/24] [z5/24C17/24C17/24] [z/24B/3] [^G/3G11/24] z/8 [z/24G/3] [E/3D11/24B11/24D11/24] z/6 [A,11/24C11/6A,11/6] z/24 A,11/24 z/24 A11/24 z/24 A,11/24 z/24\s
[=F,11/24E17/24C17/24] z/24 [z/4F,11/24] [z/4D17/24B,17/24] F,11/24 z/24 [C11/24A,11/24F,11/24] z/24 [=G,11/24B,11/6G,11/6] z/24 G,11/24 z/24 =G11/24 z/24 G,11/24 z/24\s
[E,11/24D17/24B,17/24] z/24 [z/4E,11/24] [z/4C17/24A,17/24] E11/24 z/24 [B,11/24G,11/24E,11/24] z/24 [D,11/24A,11/6F,11/6] z/24 D,11/24 z/24 D11/24 z/24 D,11/24 z/24\s
[E,11/24B,17/24G,17/24] z/24 [z/4E,11/24] [z/4C17/24A,17/24] E11/24 z/24 [D11/24B,11/24E,11/24] z/24 [F,11/24C4/3A,4/3] z/24 =F11/24 z/24 F,11/24 z/24 [E11/24A,7/3F,7/3] z/24\s
F,11/24 z/24 D11/24 z/24 F,11/24 z/24 C11/24 z/24 [A,11/24C11/6A,11/6] z/24 A,11/24 z/24 A11/24 z/24 A,11/24 z/24\s
[F,11/24E17/24C17/24] z/24 [z/4F,11/24] [z/4D17/24B,17/24] F,11/24 z/24 [C11/24A,11/24F,11/24] z/24 [G,11/24B,11/6G,11/6] z/24 G,11/24 z/24 G11/24 z/24 G,11/24 z/24\s
[E,11/24D17/24B,17/24] z/24 [z/4E,11/24] [z/4C17/24A,17/24] E11/24 z/24 [B,11/24G,11/24E,11/24] z/24 [D,11/24A,11/6F,11/6] z/24 D,11/24 z/24 D11/24 z/24 D,11/24 z/24\s
[E,11/24B,17/24G,17/24] z/24 [z/4E,11/24] [z/4C17/24A,17/24] E11/24 z/24 [D11/24B,11/24E,11/24] z/24 [A,11/24D4/3E,4/3] z/24 A11/24 z/24 A,11/24 z/24 [A11/24^C7/3E,7/3] z/24\s
A,11/24 z/24 A11/24 z/24 A,11/24 z/24 A11/24 z/24 [e11/24F,4/3E4/3=C4/3A,4/3] z/24 d11/24 z/24 c11/24 z/24 [B11/24G,4/3D4/3B,4/3G,4/3] z/24\s
c11/24 z/24 A11/24 z/24 [E,4/3D4/3B,4/3G,4/3B11/6] z/6 [z/A,4/3E4/3C4/3A,4/3] c11/24 z/24 d11/24 z/24\s
[e11/24C23/24E23/24C23/24A,23/24] z/24 d11/24 z/24 [c11/24B,23/24G23/24D23/24B,23/24] z/24 B11/24 z/24 [A11/24F,4/3A4/3F4/3C4/3] z/24 B11/24 z/24 c11/24 z/24 [d11/24G,4/3B4/3G4/3D4/3] z/24\s
c11/24 z/24 d11/24 z/24 [c11/6B11/6E11/6A17/6A,17/6] z/6 [c5/6A5/6E5/6] z/6\s
[C11/24c5/6B5/6E5/6G23/24] z/24 B,11/24 z/24 [A,11/24c5/6A5/6E5/6A23/24] z/24 E,11/24 z/24 [A11/24F,4/3e4/3c4/3A4/3] z/24 G11/24 z/24 A11/24 z/24 [c11/24G,4/3d4/3B4/3G4/3] z/24\s
B11/24 z/24 G11/24 z/24 [E4/3E,4/3e4/3B4/3G4/3] z/6 [A11/24A,4/3A4/3E4/3C4/3] z/24 G11/24 z/24 A11/24 z/24\s
[B11/24C23/24G23/24E23/24B,23/24] z/24 c11/24 z/24 [d11/24B,23/24A23/24E23/24C23/24] z/24 e11/24 z/24 [d4/3F,4/3c4/3A4/3F4/3] z/6 [c23/24G,4/3B4/3G4/3D4/3] z/24\s
B11/24 z/24 [A,4/3c11/6B11/6E11/6A119/24] z/6 A,11/24 z/24 [A,11/24c23/24A23/24E23/24] z/24 A,11/24 z/24 [A11/24c23/24B23/24E23/24] z/24\s
A,11/24 z/24 [E11/24c23/24A23/24E23/24] z/24 A11/24\s
""";
    ABCSong song;
    List<SongComponent> songComponents;
    boolean playing = false;
    float progress = 0;

    public SongPlayer() {
        client = MinecraftClient.getInstance();
    }

    public void play() {
        playing = !playing;
        progress = 0;

        song = new ABCSong()
                .setBpm(150)
                .setMeter("4/4")
                .setDefaultNoteLength("1/4");

        var parser = new ABCParser(song);

        songComponents = parser.parse(input);
    }

    public void tick() {
        progress += client.getTickDelta();
        var ms = progress * 1000;

        var passedComponents = songComponents.stream().filter(comp -> {
            return ms > comp.getEnd();
        }).toList();

        for (var comp : passedComponents) {
            if (comp instanceof SongChord) {
                for (var note : ((SongChord) comp).notes) {
                    var pitch = note.getNote() / 12 * 0.5f;
                    client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_HARP, 1, pitch);
                }
            } else if (comp instanceof SongNote) {
                var pitch = ((SongNote) comp).getNote() / 12 * 0.5f;
                client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_HARP, 1, pitch);
            }
            songComponents.remove(comp);
        }
        Log.info(LogCategory.LOG, "TIck: " + progress);
    }

    public boolean isPlaying() {
        return playing;
    }
}
