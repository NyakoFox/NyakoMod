package gay.nyako.nyakomod.test;

import gay.nyako.nyakomod.InstrumentRegister;
import gay.nyako.nyakomod.InstrumentRegistry;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.block.NoteBlockPlusBlockEntity;
import me.stupidcat.abcparser.ABCParser;
import me.stupidcat.abcparser.ABCSong;
import me.stupidcat.abcparser.struct.SongChord;
import me.stupidcat.abcparser.struct.SongComponent;
import me.stupidcat.abcparser.struct.SongNote;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.List;

public class SongPlayer {
    MinecraftClient client;
    NoteBlockPlusBlockEntity blockEntity;

    public static String megalovania = """
                        X: 1
                        d/4 d/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
                        c/4 c/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
                        B/4 B/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
                        _B/4 B/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
                        [d/4D/4D4F4] [d/4D/4] [d'/D/] [D/4a3/4] D/ [^g/D/] [=g/D/] [f/D/] [d/4D/4] [f/4D/4] [g/4D/4]\s
                        [c/4C/4C4E4] [c/4C/4] [d'/C/] [C/4a3/4] C/ [^g/C/] [=g/C/] [f/C/] [d/4C/4] [f/4C/4] [g/4C/4]\s
                        [=B/4B,/4G,4B,4] [B/4B,/4] [d'/B,/] [B,/4a3/4] B,/ [^g/B,/] [=g/B,/] [f/B,/] [d/4B,/4] [f/4B,/4] [g/4B,/4]\s
                        [_B/4_B,/4B,7/4D7/4] [B/4B,/4] [d'/B,/] [B,/4a3/4] B,/ [^g/C/C2E2] [=g/C/] [f/C/] [d/4C/4] [f/4C/4] [g/4C/4]\s
                        [d/4d'/4D/4D/4F/4A/4d/4] [d/4d'/4D/4D/4F/4A/4d/4] [d'/d''/D/d/f/a/d'/] [D/4a3/4a'3/4A3/4d3/4f3/4a3/4] D/ [^g/^g'/D/^G/c/f/g/] [=g/=g'/D/=G/c/d/g/] [f/f'/D/F/^G/c/f/] [d/4d'/4D/4D/4F/4G/4d/4] [f/4f'/4D/4F/4G/4c/4f/4] [g/4g'/4D/4=G/4c/4d/4g/4]\s
                        [c/4c'/4C/4C/4E/4G/4c/4] [c/4c'/4C/4C/4E/4G/4c/4] [d'/d''/C/d/f/a/d'/] [C/4a3/4a'3/4A3/4d3/4f3/4a3/4] C/ [^g/^g'/C/^G/c/f/g/] [=g/=g'/C/=G/c/d/g/] [f/f'/C/F/^G/c/f/] [d/4d'/4C/4D/4F/4G/4d/4] [f/4f'/4C/4F/4G/4c/4f/4] [g/4g'/4C/4=G/4c/4d/4g/4]\s
                        [=B/4b/4=B,/4B,/4D/4F/4B/4] [B/4b/4B,/4B,/4D/4F/4B/4] [d'/d''/B,/d/f/a/d'/] [B,/4a3/4a'3/4A3/4d3/4f3/4a3/4] B,/ [^g/^g'/B,/^G/c/f/g/] [=g/=g'/B,/=G/c/d/g/] [f/f'/B,/F/^G/c/f/] [d/4d'/4B,/4D/4F/4G/4d/4] [f/4f'/4B,/4F/4G/4c/4f/4] [g/4g'/4B,/4=G/4c/4d/4g/4]\s
                        [_B/4_b/4_B,/4D/4F/4F/4B/4] [B/4b/4B,/4D/4F/4F/4B/4] [d'/d''/B,/d/f/a/d'/] [B,/4a3/4a'3/4A3/4d3/4f3/4a3/4] B,/ [^g/^g'/C/^G/c/f/g/] [=g/=g'/C/=G/c/d/g/] [f/f'/C/F/^G/c/f/] [d/4d'/4C/4D/4F/4G/4d/4] [f/4f'/4C/4F/4G/4c/4f/4] [g/4g'/4C/4=G/4c/4d/4g/4]\s
                        [d/4d'/4D/4D/4F/4A/4d/4] [d/4d'/4D/4D/4F/4A/4d/4] [d'/d''/D/d/f/a/d'/] [D/4a3/4a'3/4A3/4d3/4f3/4a3/4] D/ [^g/^g'/D/^G/c/f/g/] [=g/=g'/D/=G/c/d/g/] [f/f'/D/F/^G/c/f/] [d/4d'/4D/4D/4F/4G/4d/4] [f/4f'/4D/4F/4G/4c/4f/4] [g/4g'/4D/4=G/4c/4d/4g/4]\s
                        [c/4c'/4C/4C/4E/4G/4c/4] [c/4c'/4C/4C/4E/4G/4c/4] [d'/d''/C/d/f/a/d'/] [C/4a3/4a'3/4A3/4d3/4f3/4a3/4] C/ [^g/^g'/C/^G/c/f/g/] [=g/=g'/C/=G/c/d/g/] [f/f'/C/F/^G/c/f/] [d/4d'/4C/4D/4F/4G/4d/4] [f/4f'/4C/4F/4G/4c/4f/4] [g/4g'/4C/4=G/4c/4d/4g/4]\s
                        [=B/4=b/4=B,/4B,/4D/4F/4B/4] [B/4b/4B,/4B,/4D/4F/4B/4] [d'/d''/B,/d/f/a/d'/] [B,/4a3/4a'3/4A3/4d3/4f3/4a3/4] B,/ [^g/^g'/B,/^G/c/f/g/] [=g/=g'/B,/=G/c/d/g/] [f/f'/B,/F/^G/c/f/] [d/4d'/4B,/4D/4F/4G/4d/4] [f/4f'/4B,/4F/4G/4c/4f/4] [g/4g'/4B,/4=G/4c/4d/4g/4]\s
                        [_B/4_b/4_B,/4D/4F/4F/4B/4] [B/4b/4B,/4D/4F/4F/4B/4] [d'/d''/B,/d/f/a/d'/] [B,/4a3/4a'3/4A3/4d3/4f3/4a3/4] B,/ [^g/^g'/C/^G/c/f/g/] [=g/=g'/C/=G/c/d/g/] [f/f'/C/F/^G/c/f/] [_B,,/16d/4d'/4C/4D/4F/4] C,/16 D,/16 E,/16 [F,/16f/4f'/4C/4F/4G/4] G,/16 A,/16 B,/16 [C/16g/4g'/4C/4=G/4c/4] D/16 E/16 F/16\s
                        [D/4F/D7/F7/] D/4 [F/4D/] [z/4F/] D/4 [D/F/] [D/F/] [D/D/] [D/D3/4] D/4 [D/4d/4D/F/D/] D/4\s
                        [C/4F/4C7/E7/] [C/4F/4] [F/4C/] [z/4F/] C/4 [C/G/] [C/^G/] [=G/4C/] F/4 [D/4C/] F/4 [C/4G3/4] [C/4c/4C/E/] C/4\s
                        [=B,/4F/G,7/B,7/] B,/4 [F/4B,/] [z/4F/] B,/4 [B,/G/] [B,/^G/] [B,/A/] [B,/c/] [B,/4A3/4] [B,/4=B/4G,/B,/] B,/4\s
                        [_B,/4d/B,7/4D7/4] B,/4 [B,/d/] [B,/4d/4] [A/4B,/] d/4 [C/C2E2c2] C/ C/ C/4 C/4 C/4\s
                        [d/4D/4F/A/D7/F7/] [d/4D/4] [F/4A/4d'/D/] [z/4F/A/] [D/4a3/4] [D/F/A/] [^g/D/F/A/] [=g/D/D/=G/] [f/D/DG] [d/4D/4] [f/4D/4D/F/] [g/4D/4]\s
                        [c/4C/4F/A/C7/E7/] [c/4C/4] [F/4A/4d'/C/] [z/4F/A/] [C/4a3/4] [C/F/A/] [^g/C/E/G/] [=g/C/F/A/] [f/C/A/d/] [d/4C/4F/4A/4] [f/4C/4E/4G/4C/E/] [g/4C/4]\s
                        [B/4D/4=B,/4A/d/] [B/4G/4B,/4] [C/4d'/B,/F/A/] G/4 [B,/4B,/4E/G/a3/4] [G/4B,/] [A,/4D/F/] [G/4^g/B,/] [D/4G/c/] [F/4=g/B,/] [C/4F/A/] [F/4f/B,/] [B,/4D/G/] [d/4F/4B,/4] [f/4A,/4B,/4C/F/] [g/4F/4B,/4]\s
                        [_B/4_B,/4D/B,7/4D7/4] [B/4B,/4] [E/4d'/B,/] [z/4F/] [B,/4a3/4] [B,/A/] [^g/C/C2E2A2c2] [=g/C/] [f/C/] [d/4C/4] [f/4C/4] [g/4C/4]\s
                        [D/4D4F4] D/4 D/ D/4 D/ [z/4D/] [f/4f/4] [d/4d/4D/] [f/4f/4] [g/4g/4D/] [^g/4g/4] [=g/4g/4D/4] [f/4f/4D/4] [d/4d/4D/4]\s
                        [^g/8g/8C/4C4E4] [=g/8g/8] [f/8f/8C/4] [d/8d/8] [f/f/C/] [C/4g2g2] C/ C/ C/ C/ [C/4^g/g/] C/4 [a/4a/4C/4]\s
                        [=B,/4c'/c'/G,4B,4] B,/4 [a/4a/4B,/] [g/4g/4] [=g/4g/4B,/4] [f/4f/4B,/] [d/4d/4] [e/4e/4B,/] [z/4f/f/] [z/4B,/] [z/4g/g/] [z/4B,/] [z/4a/a/] B,/4 [B,/4c'/c'/] B,/4\s
                        [^C/4^c'/c'/C7/4F7/4] C/4 [^g/g/C/] [g/4g/4C/4] [=g/4g/4C/] [f/4f/4] [g/4g/4^D/D2G2] z/4 D/ D/ D/4 D/4 D/4\s
                        [=D/4f/F/D4F4] D/4 [g/G/D/] [D/4a/A/] [z/4D/] [z/4d'/f'/d/f/] [z/4D/] [z/4=c'e'ce] D/ [z/4D/] [z/4bd'Bd] D/4 D/4 D/4\s
                        [=C/4c'e'ceC4E4] C/4 C/ [C/4d'f'df] C/ [z/4C/] [z/4e'g'eg] C/ [z/4C/] [z/4c'e'ce] C/4 C/4 C/4\s
                        [B,/4d'2a'2d2a2G,4B,4] B,/4 B,/ B,/4 B,/ [z/4B,/] [a'/4a/4] [^g'/4^g/4B,/] [=g'/4=g/4] [^f'/4^f/4B,/] [=f'/4=f/4] [e'/4e/4B,/4] [^d'/4^d/4B,/4] [=d'/4=d/4B,/4]\s
                        [^C/4f2^c'2C2F2F2^c2] C/4 C/ C/4 C/ [z/4^D/] [z/4g2^d'2D2G2G2^d2] D/ D/ D/4 D/4 D/4\s
                        [=D/4D4F4] D/4 D/ D/4 D/ [z/4D/] [f/4f/4] [=d/4d/4D/] [f/4f/4] [g/4g/4D/] [^g/4g/4] [=g/4g/4D/4] [f/4f/4D/4] [d/4d/4D/4]\s
                        [^g/8g/8=C/4C4E4] [=g/8g/8] [f/8f/8C/4] [d/8d/8] [f/f/C/] [C/4g2g2] C/ C/ C/ C/ [C/4^g/g/] C/4 [a/4a/4C/4]\s
                        [B,/4=c'/c'/G,4B,4] B,/4 [a/4a/4B,/] [g/4g/4] [=g/4g/4B,/4] [f/4f/4B,/] [d/4d/4] [e/4e/4B,/] [z/4f/f/] [z/4B,/] [z/4g/g/] [z/4B,/] [z/4a/a/] B,/4 [B,/4c'/c'/] B,/4\s
                        [^C/4^c'/c'/C7/4F7/4] C/4 [^g/g/C/] [g/4g/4C/4] [=g/4g/4C/] [f/4f/4] [g/4g/4^D/D2G2] z/4 D/ D/ D/4 D/4 D/4\s
                        [=D/4f/F/D4F4] D/4 [g/G/D/] [D/4a/A/] [z/4D/] [z/4=d'/f'/d/f/] [z/4D/] [z/4=c'e'=ce] D/ [z/4D/] [z/4bd'Bd] D/4 D/4 D/4\s
                        [=C/4c'e'ceC4E4] C/4 C/ [C/4d'f'df] C/ [z/4C/] [z/4e'g'eg] C/ [z/4C/] [z/4c'e'ce] C/4 C/4 C/4\s
                        [B,/4d'2a'2d2a2G,4B,4] B,/4 B,/ B,/4 B,/ [z/4B,/] [a'/4a/4] [^g'/4^g/4B,/] [=g'/4=g/4] [^f'/4^f/4B,/] [=f'/4=f/4] [e'/4e/4B,/4] [^d'/4^d/4B,/4] [=d'/4=d/4B,/4]\s
                        [^C/4f2^c'2C2F2F2^c2] C/4 C/ C/4 C/ [z/4^D/] [z/4g2^d'2=D2G2G2^d2] ^D/ D/ D/4 D/4 D/4\s
                        [_B,/=D/B,3] [B,/D/] [B,/4D/4] [B,/D/] [B,/D/] [B,/D/] [B,/4D/4] [B,/D/F] [B,/D/]\s
                        [=C/E/E2] [C/E/] [C/4E/4] [C/E/] [z/4C/E/] [z/4D2] [C/E/] [C/4E/4] [C/E/] [C/E/]\s
                        [G,/=B,/F8] [G,/B,/] [G,/4B,/4] [G,/B,/] [G,/B,/] [G,/B,/] [G,/4B,/4] [G,/B,/] [G,/B,/]\s
                        [G,/B,/] [G,/B,/] [G,/4B,/4] [G,/B,/] [G,/B,/] [G,/B,/] [G,/4B,/4] [G,/B,/] [G,/B,/]\s
                        [_B,/D/B,3] [B,/D/] [B,/4D/4] [B,/D/] [B,/D/] [B,/D/] [B,/4D/4] [B,/D/F] [B,/D/]\s
                        [C/E/E2] [C/E/] [C/4E/4] [C/E/] [z/4C/E/] [z/4D2] [C/E/] [C/4E/4] [C/E/] [C/E/]\s
                        [D/F/D8] [D/F/] [D/4F/4] [D/F/] [D/F/] [D/F/] [D/4F/4] [D/F/] [D/F/]\s
                        [D/F/] [D/F/] [D/4F/4] [D/F/] [D/F/] [D/F/] [D/4F/4] [D/F/] [D/F/]\s
                        [D/4B,/D/B,3] D/4 [B,/D/=d/] [B,/4D/4A3/4] [B,/D/] [B,/D/^G/] [B,/D/=G/] [B,/4D/4F/] [z/4B,/D/F] D/4 [F/4B,/D/] G/4\s
                        [C/4C/E/E2] C/4 [C/E/d/] [C/4E/4A3/4] [C/E/] [z/4C/E/^G/] [z/4D2] [C/E/=G/] [C/4E/4F/] [z/4C/E/] D/4 [F/4C/E/] G/4\s
                        [=B,/4G,/B,/F8] B,/4 [G,/B,/d/] [G,/4B,/4A3/4] [G,/B,/] [G,/B,/^G/] [G,/B,/=G/] [G,/4B,/4F/] [z/4G,/B,/] D/4 [F/4G,/B,/] G/4\s
                        [_B,/4G,/=B,/] [=d'/4_B,/4] [f'/4G,/=B,/d/] d'/4 [G,/4B,/4g'/A3/4] [z/4G,/B,/] [z/4f'/] [z/4G,/B,/^G/] d'/4 [=c'/G,/B,/=G/] [G,/4B,/4a/F/] [z/4G,/B,/] [g/4D/4] [a/4F/4G,/B,/] [c'/4G/4]\s
                        [D/4_B,/D/B,3] D/4 [B,/D/d/] [B,/4D/4A3/4] [B,/D/] [B,/D/^G/] [B,/D/=G/] [B,/4D/4F/] [z/4B,/D/F] D/4 [F/4B,/D/] G/4\s
                        [C/4C/E/E2] C/4 [C/E/d/] [C/4E/4A3/4] [C/E/] [z/4C/E/^G/] [z/4D2] [C/E/=G/] [C/4E/4F/] [z/4C/E/] D/4 [F/4C/E/] G/4\s
                        [=B,/4D/F/D8] B,/4 [d'/f'/D/F/d/] [D/4F/4c'3/4e'3/4A3/4] [D/F/] [g/c'/D/F/^G/] [c'/e'/D/F/=G/] [D/4F/4a/d'/F/] [z/4D/F/] [e/4g/4D/4] [f/4a/4F/4D/F/] [g/4c'/4G/4]\s
                        [_B,/4D/F/] B,/4 [d'/f'/D/F/d/] [D/4F/4c'3/4e'3/4A3/4] [D/F/] [g/c'/D/F/^G/] [c'/e'/D/F/=G/] [D/4F/4a/d'/F/] [z/4D/F/] [e/4g/4D/4] [f/4a/4F/4D/F/] [g/4c'/4G/4]\s
                        [B,/D/] [B,/D/] [B,/4D/4] [B,/D/] [B,/D/] [B,/D/] [B,/D/] [B,/4D/4] [B,/4D/4] [B,/4D/4]\s
                        [C/E/] [C/E/] [C/4E/4] [C/E/] [C/E/] [C/E/] [C/E/] [C/4E/4] [C/4E/4] [C/4E/4]\s
                        [D/F/] [D/F/] [D/4F/4] [D/F/] [^C/F/] [C/F/] [C/F/] [C/4F/4] [C/F/]\s
                        [=C/E/] [C/E/] [C/4E/4] [C/E/] [=B,/^D/] [B,/D/] [B,/D/] [B,/4D/4] [B,/D/]\s
                        [_B,/=D/] [B,/D/] [B,/4D/4] [B,/D/] [B,/D/] [B,/D/] [B,/D/] [B,/4D/4] [B,/4D/4] [B,/4D/4]\s
                        [C/E/] [C/E/] [C/4E/4] [C/E/] [C/E/] [C/E/] [C/E/] [C/4E/4] [C/4E/4] [C/4E/4]\s
                        [D/F/] [D/F/] [D/4F/4] [D/F/] [D/F/] [D/F/] [D/F/] [D/4F/4] [D/4F/4] [D/4F/4]\s
                        [D/F/] [D/F/] [D/4F/4] [D/F/] [D/F/] [D/F/] [D/F/] [D/4F/4] [D/4F/4] [D/4F/4]\s
                        [B,/D/] [B,/D/] [B,/4D/4] [B,/D/] [B,/D/] [B,/D/] [B,/D/] [B,/4D/4] [B,/4D/4] [B,/4D/4]\s
                        [C/E/] [C/E/] [C/4E/4] [C/E/] [C/E/] [C/E/] [C/E/] [C/4E/4] [C/4E/4] [C/4E/4]\s
                        [D/F/] [D/F/] [D/4F/4] [D/F/] [^C/F/] [C/F/] [C/F/] [C/4F/4] [C/F/]\s
                        [=C/E/] [C/E/] [C/4E/4] [C/E/] [=B,/^D/] [B,/D/] [B,/D/] [B,/4D/4] [B,/D/]\s
                        [_B,/=D/] [B,/D/] [B,/4D/4] [B,/D/] [B,/D/] [B,/D/] [B,/D/] [B,/4D/4] [B,/4D/4] [B,/4D/4]\s
                        [C/E/] [C/E/] [C/4E/4] [C/E/] [C/E/] [C/E/] [C/E/] [C/4E/4] [C/4E/4] [C/4E/4]\s
                        [d/4D/F/] d/4 [d'/D/F/] [D/4F/4a3/4] [D/F/] [^g/D/F/] [=g/D/F/] [f/D/F/] [d/4D/4F/4] [f/4D/4F/4] [g/4D/4F/4]\s
                        [d/4D/F/] d/4 [d'/D/F/] [D/4F/4a3/4] [D/F/] [^g/D/F/] [=g/D/F/] [f/D/F/] [d/4D/4F/4] [f/4D/4F/4] [g/4D/4F/4]\s
                        B/4 B/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
                        =c/4 c/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
                        d/4 d/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
                        d/4 d/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
                        B/4 B/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
                        c/4 c/4 d'/ a3/4 ^g/ =g/ f/ d/4 f/4 g/4\s
            """;

    public static String test = """
                C D E F G A B c d e f g a b
            """;

    ABCSong song;
    SongComponent[] songComponents;
    boolean playing = false;
    long initial = 0;
    int pointer = 0;
    float progress = 0;

    public SongPlayer() {
        client = MinecraftClient.getInstance();
    }

    public SongPlayer(NoteBlockPlusBlockEntity blockEntity) {
        client = MinecraftClient.getInstance();
        this.blockEntity = blockEntity;
    }

    public void play() {
        play("""
                    X: 1
                    [D/8^G,/8D,,5/24D5/24] z/24 [z/12F/8B,/8] [z/12F,,5/24F5/24] [^G/8D/8] z/24 [B/8F/8^G,,5/24G5/24] z/24 [z/12d/8G/8] [z/12B,,5/24B5/24] [f/8B/8] z/24 [^D/8A,/8F,,5/24F5/24] z/24 [z/12^F/8C/8] [z/12G,,5/24G5/24] [A/8D/8] z/24 [c/8F/8C,,5/24C5/24] z/24 [z/12^d/8A/8] [z/12^D,,5/24D5/24] [^f/8c/8] z/24 [=F/8B,/8^F,,5/24^F5/24] z/24 [z/12G/8=D/8] [z/12A,,5/24A5/24] [B/8=F/8] z/24 [=d/8G/8F,,5/24^F5/24] z/24 [z/12=f/8B/8] [z/12D,,5/24^D5/24] [^g/8d/8] z/24 [^d/8A/8B,,,5/24B,5/24] z/24 [z/12^f/8c/8] [z/12=D,,5/24=D5/24] [a/8d/8] z/24 [c'/8f/8=F,,5/24=F5/24] z/24 [z/12^d'/8a/8] [z/12G,,5/24G5/24] [^f'/8c'/8] z/24\s
                    [A,5/12A,,,5/12e7/6c7/6E23/6A,23/6] z/12 [A5/24A,,5/24] z/24 [A,5/12A,,,5/12] z/12 [=d5/24A,5/24A,,,5/24B5/24] z/24 [c5/24A5/24A,,5/24A5/24] z/24 [d5/24A,5/24A,,,5/24B5/24] z/24 [A5/24A,,5/24e23/24c23/24] z/24 [A,5/24A,,,5/24] z/24 [A5/24A,,5/24] z/24 [z/4A,,,11/24A,11/24] [z/4a23/24e23/24] [A,5/24A,,,5/24] z/24 [A5/24A,,5/24] z/24 [A,,,5/24A,5/24] z/24\s
                    [=G,5/12G,,,5/12=g7/6d7/6D23/6G,23/6] z/12 [=G5/24=G,,5/24] z/24 [G,5/12G,,,5/12] z/12 [=f5/24G,5/24G,,,5/24d5/24] z/24 [e5/24F,5/24F,,,5/24c5/24] z/24 [d5/24G,5/24G,,,5/24B5/24] z/24 [G,5/24G,,,5/24c23/24A23/24] z/24 [G,5/24G,,,5/24] z/24 [G5/24G,,5/24] z/24 [z/4G,,,11/24G,11/24] [z/4d23/24B23/24] [G,5/24G,,,5/24] z/24 [G5/24G,,5/24] z/24 [G,,,5/24G,5/24] z/24\s
                    [B,5/12B,,,5/12^d7/6B7/6^D11/6^F,11/6] z/12 [B,5/24B,,,5/24] z/24 [B5/12B,,5/12] z/12 [d5/24B,5/24B,,,5/24B5/24] z/24 [e5/24^F5/24^F,,5/24B5/24] z/24 [^f5/24B,,5/24B5/24d5/24] z/24 [E,5/24E,,,5/24e11/6G,11/6E11/6B11/6] z/24 [E,5/24E,,,5/24] z/24 [E5/24E,,5/24] z/24 [E,11/24E,,,11/24] z/24 [E,5/24E,,,5/24] z/24 [E5/24E,,5/24] z/24 [E,,,5/24E,5/24] z/24\s
                    [D,5/12D,,,5/12f7/6=d7/6=D11/6F,11/6] z/12 [D,5/24D,,,5/24] z/24 [D5/12D,,5/12] z/12 [f5/24D,5/24D,,,5/24d5/24] z/24 [g5/24A,5/24A,,,5/24e5/24] z/24 [a5/24D,,5/24D5/24f5/24] z/24 [G,5/24G,,,5/24g23/24G,23/24D23/24d23/24] z/24 [G,5/24G,,,5/24] z/24 [G5/24G,,5/24] z/24 [G,5/24G,,,5/24] z/24 [^G,5/24^G,,,5/24^g23/24G,23/24E23/24e23/24] z/24 [E5/24E,,5/24] z/24 [^G5/24^G,,5/24] z/24 [G,5/24G,,,5/24] z/24\s
                    [a5/24e5/24A,,,11/24A,11/24E23/6A,23/6] z/24 [e5/24c5/24] z/24 [a5/24e5/24A,,,11/24A,11/24] z/24 [b5/24e5/24] z/24 [e5/24=G,5/24=G,,,5/24c5/24] z/24 [b5/24e5/24A,,,11/24A,11/24] z/24 [z/4c'5/8a5/8] [A,,,11/24A,11/24] z/24 [e'5/24G,5/24G,,,5/24c'5/24] z/24 [=d'5/24A,5/24A,,,5/24b5/24] z/24 [c'5/24G,5/24G,,,5/24a5/24] z/24 [b5/24c5/24C,5/24=g5/24] z/24 [a5/24B5/24B,,5/24=f5/24] z/24 [b5/24A5/24A,,5/24g5/24] z/24 [c'5/24=G,,5/24=G5/24a5/24] z/24\s
                    [G,5/24G,,,5/24d'17/24g17/24D11/6G,11/6] z/24 [G,5/24G,,,5/24] z/24 [G5/24G,,5/24] z/24 [G,5/24G,,,5/24g17/24d17/24] z/24 [D5/24D,,5/24] z/24 [G,5/24G,,,5/24] z/24 [G5/24G,,5/24d'11/24g11/24] z/24 [G,,,5/24G,5/24] z/24 [=F,5/24F,,,5/24c'17/24f17/24C11/6F,11/6] z/24 [F,5/24F,,,5/24] z/24 [=F5/24=F,,5/24] z/24 [F,5/24F,,,5/24b17/24f17/24] z/24 [C5/24C,,5/24] z/24 [F5/24F,,5/24] z/24 [F,5/24F,,,5/24a11/24f11/24] z/24 [F,,5/24F5/24] z/24\s
                    [E,5/24E,,,5/24b11/6A,11/6E11/6e11/6] z/24 [E,5/24E,,,5/24] z/24 [E5/24E,,5/24] z/24 [E,5/24E,,,5/24] z/24 [B,5/24B,,,5/24] z/24 [E,5/24E,,,5/24] z/24 [E5/24E,,5/24] z/24 [E,,,5/24E,5/24] z/24 [E5/24E,,5/24^g17/24e17/24E11/6^G,11/6] z/24 [E,5/24E,,,5/24] z/24 [E,5/24E,,,5/24] z/24 [D5/24D,,5/24a17/24f17/24] z/24 [E,5/24E,,,5/24] z/24 [E,5/24E,,,5/24] z/24 [E5/24E,,5/24b11/24g11/24] z/24 [E,,,5/24E,5/24] z/24\s
                    [c'5/24A,5/24A,,,5/24a5/24E17/24A,17/24] z/24 [a5/24E5/24E,,5/24e5/24] z/24 [e5/24A5/24A,,5/24c5/24] z/24 [b5/24=G,5/24G,,,5/24=g5/24D17/24G,17/24] z/24 [g5/24D5/24D,,5/24d5/24] z/24 [d5/24G5/24G,,5/24B5/24] z/24 [F,17/24F,,,17/24a4/3f4/3C7/3F,7/3] z/24 [F,5/24F,,,5/24] z/24 [F5/24F,,5/24] z/24 [F,,,5/24F,5/24] z/24 [F,5/24F,,,5/24g11/24e11/24] z/24 [C5/24C,,5/24] z/24 [F5/24F,,5/24a11/24f11/24] z/24 [F,,,5/24F,5/24] z/24\s
                    [A,5/12A,,,5/12e7/6c7/6E23/6A,23/6] z/12 [A5/24A,,5/24] z/24 [A,5/12A,,,5/12] z/12 [d5/24A,5/24A,,,5/24B5/24] z/24 [c5/24G,5/24G,,,5/24A5/24] z/24 [d5/24A,5/24A,,,5/24B5/24] z/24 [A,5/24A,,,5/24e23/24c23/24] z/24 [A,5/24A,,,5/24] z/24 [A5/24A,,5/24] z/24 [z/4A,,,11/24A,11/24] [z/4a23/24e23/24] [A,5/24A,,,5/24] z/24 [A5/24A,,5/24] z/24 [A,,,5/24A,5/24] z/24\s
                    [G,5/12G,,,5/12g7/6d7/6D23/6G,23/6] z/12 [G5/24G,,5/24] z/24 [G,5/12G,,,5/12] z/12 [f5/24G,5/24G,,,5/24d5/24] z/24 [e5/24F,5/24F,,,5/24c5/24] z/24 [d5/24G,5/24G,,,5/24B5/24] z/24 [G,5/24G,,,5/24c23/24A23/24] z/24 [G,5/24G,,,5/24] z/24 [G5/24G,,5/24] z/24 [z/4G,,,11/24G,11/24] [z/4d23/24B23/24] [G,5/24G,,,5/24] z/24 [G5/24G,,5/24] z/24 [G,,,5/24G,5/24] z/24\s
                    [B,5/12B,,,5/12^d7/6B7/6^D11/6^F,11/6] z/12 [B,5/24B,,,5/24] z/24 [B5/12B,,5/12] z/12 [d5/24B,5/24B,,,5/24B5/24] z/24 [e5/24^F5/24^F,,5/24B5/24] z/24 [^f5/24B,,5/24B5/24d5/24] z/24 [E,5/24E,,,5/24e11/6G,11/6E11/6B11/6] z/24 [E,5/24E,,,5/24] z/24 [E5/24E,,5/24] z/24 [E,11/24E,,,11/24] z/24 [E,5/24E,,,5/24] z/24 [E5/24E,,5/24] z/24 [E,,,5/24E,5/24] z/24\s
                    [D,5/12D,,,5/12f7/6=d7/6=D11/6F,11/6] z/12 [D,5/24D,,,5/24] z/24 [D5/12D,,5/12] z/12 [f5/24D,5/24D,,,5/24d5/24] z/24 [g5/24A,5/24A,,,5/24e5/24] z/24 [a5/24D,,5/24D5/24f5/24] z/24 [G,5/24G,,,5/24g23/24G,23/24D23/24d23/24] z/24 [G,5/24G,,,5/24] z/24 [G5/24G,,5/24] z/24 [G,5/24G,,,5/24] z/24 [^G,5/24^G,,,5/24^g23/24G,23/24E23/24e23/24] z/24 [E5/24E,,5/24] z/24 [^G5/24^G,,5/24] z/24 [G,5/24G,,,5/24] z/24\s
                    [a5/24e5/24A,,,11/24A,11/24E23/6A,23/6] z/24 [e5/24c5/24] z/24 [a5/24e5/24A,,,11/24A,11/24] z/24 [b5/24e5/24] z/24 [e5/24=G,5/24=G,,,5/24c5/24] z/24 [b5/24e5/24A,,,11/24A,11/24] z/24 [z/4c'5/8a5/8] [A,,,11/24A,11/24] z/24 [e'5/24G,5/24G,,,5/24c'5/24] z/24 [d'5/24A,5/24A,,,5/24b5/24] z/24 [c'5/24G,5/24G,,,5/24a5/24] z/24 [b5/24c5/24C,5/24=g5/24] z/24 [a5/24B5/24B,,5/24=f5/24] z/24 [b5/24A5/24A,,5/24g5/24] z/24 [c'5/24=G,,5/24=G5/24a5/24] z/24\s
                    [G,5/24G,,,5/24d'17/24g17/24D11/6G,11/6] z/24 [G,5/24G,,,5/24] z/24 [G5/24G,,5/24] z/24 [G,5/24G,,,5/24g17/24d17/24] z/24 [D5/24D,,5/24] z/24 [G,5/24G,,,5/24] z/24 [G5/24G,,5/24d'11/24g11/24] z/24 [G,,,5/24G,5/24] z/24 [=F,5/24F,,,5/24c'17/24f17/24C11/6F,11/6] z/24 [F,5/24F,,,5/24] z/24 [=F5/24=F,,5/24] z/24 [F,5/24F,,,5/24b17/24f17/24] z/24 [C5/24C,,5/24] z/24 [F5/24F,,5/24] z/24 [F,5/24F,,,5/24a11/24f11/24] z/24 [F,,5/24F5/24] z/24\s
                    [E,5/24E,,,5/24b11/6A,11/6E11/6e11/6] z/24 [E,5/24E,,,5/24] z/24 [E5/24E,,5/24] z/24 [E,5/24E,,,5/24] z/24 [B,5/24B,,,5/24] z/24 [E,5/24E,,,5/24] z/24 [E5/24E,,5/24] z/24 [E,,,5/24E,5/24] z/24 [E5/24E,,5/24^g17/24e17/24E11/6^G,11/6] z/24 [E,5/24E,,,5/24] z/24 [E,5/24E,,,5/24] z/24 [D5/24D,,5/24a17/24f17/24] z/24 [E,5/24E,,,5/24] z/24 [E,5/24E,,,5/24] z/24 [E5/24E,,5/24b11/24g11/24] z/24 [E,,,5/24E,5/24] z/24\s
                    [F5/24F,,,11/24F,11/24E11/6A,23/6F23/6] z/24 A5/24 [z/24F5/24] [c5/24F,,5/24F5/24] [z/24A5/24] [e5/24F,,,11/24F,11/24] [z/24c5/24] f5/24 [z/24e5/24] [a5/24F,,,5/24F,5/24] [z/24f5/24] [c'5/24F,,5/24F5/24] [z/24a5/24] [e'5/24F,5/24F,,,5/24] [z/24c'5/24] [c'5/24F,,,11/24F,11/24D23/24] [z/24e'5/24] a5/24 [z/24c'5/24] [f5/24F,,5/24F5/24] [z/24a5/24] [e5/24F,,,11/24F,11/24] [z/24f5/24] [c5/24G23/24] [z/24e5/24] [A5/24F,,,5/24F,5/24] [z/24c5/24] [F5/24F,,5/24F5/24] [z/24A5/24] [A5/24F,,,5/24F,5/24] [z/24F5/24]\s
                    [D5/24E,,,11/24E,11/24E17/24E23/6=G,23/6] [z/24A5/24] G5/24 [z/24D5/24] [B5/24E,,5/24E5/24] [z/24G5/24] [d5/24E,,,11/24E,11/24B,7/6] [z/24B5/24] =g5/24 [z/24d5/24] [b5/24E,,,5/24E,5/24] [z/24g5/24] [d'5/24E,,5/24E5/24] [z/24b5/24] [b5/24E,,,5/24E,5/24] [z/24d'5/24] [g'5/24E,,,11/24E,11/24B,17/24] [z/24b5/24] d'5/24 [z/24g'5/24] [b5/24E,,5/24E5/24] [z/24d'5/24] [g5/24E,,,11/24E,11/24C17/24] [z/24b5/24] B5/24 [z/24g5/24] [d5/24E,,,5/24E,5/24] [z/24B5/24] [G5/24E,,5/24E5/24D11/24] [z/24d5/24] [B5/24E,,,5/24E,5/24] [z/24G5/24]\s
                    [F5/24F,,,11/24F,11/24E11/6F23/6A,23/6] [z/24B5/24] A5/24 [z/24F5/24] [c5/24F,,5/24F5/24] [z/24A5/24] [e5/24F,,,11/24F,11/24] [z/24c5/24] f5/24 [z/24e5/24] [a5/24F,,,5/24F,5/24] [z/24f5/24] [c'5/24F,,5/24F5/24] [z/24a5/24] [e'5/24F,5/24F,,,5/24] [z/24c'5/24] [=f'5/24F,,,11/24F,11/24D5/6] [z/24e'5/24] e'5/24 [z/24f'5/24] [c'5/24F,,5/24F5/24] [z/24e'5/24] [a5/24F,11/24F,,,11/24] [z/24c'5/24] [f5/24G5/6] [z/24a5/24] [e5/24F,,,5/24F,5/24] [z/24f5/24] [c5/24F,,5/24F5/24] [z/24e5/24] [A5/24F,,,5/24F,5/24] [z/24c5/24]\s
                    [G5/24E,,,11/24E,11/24E17/24G11/6B,23/6] [z/24A5/24] B5/24 [z/24G5/24] [e5/24E,,5/24E5/24] [z/24B5/24] [g5/24E,,,11/24E,11/24^F17/24] [z/24e5/24] b5/24 [z/24g5/24] [e'5/24E,,,5/24E,5/24] [z/24b5/24] [g'5/24E,,5/24E5/24G/3] [z/24e'5/24] [e'5/24E,,,5/24E,5/24] [z/24g'5/24] [^g'5/24E,,,11/24E,11/24^G17/24G11/6] [z/24e'5/24] e'5/24 [z/24g'5/24] [b5/24E,,5/24E5/24] [z/24e'5/24] [^g5/24E,,,11/24E,11/24A17/24] [z/24b5/24] e5/24 [z/24g5/24] [B5/24E,,,5/24E,5/24] [z/24e5/24] [G5/24E,,5/24E5/24B/3] [z/24B5/24] [E5/24E,,,5/24E,5/24] z/24\s
                    [c5/24A,,,5/12A,5/12E,11/6A,11/6C11/6] z/24 B5/24 [z/24c5/24] [A,,5/24A5/24c4/3] [z/24B5/24] [z5/24A,,,5/12A,5/12] [z7/24c4/3] [A,5/24A,,,5/24] z/24 [G,5/24G,,,5/24] z/24 [A,,,5/24A,5/24] z/24 [A,5/24A,,,5/24B17/24D11/6G,11/6B,11/6] z/24 [A,,,5/24A,5/24] [z/24B17/24] [A5/24A,,5/24] z/24 [A,,,11/24A,11/24A17/24] [z/24A17/24] [A,5/24A,,,5/24] z/24 [A5/24A,,5/24=G11/24] z/24 [A,,,5/24A,5/24] [z/24G11/24]\s
                    [D5/12D,,5/12A23/6D23/6^F,23/6A,23/6] z/24 [z/24A23/6] [d5/24D,5/24] z/24 [D5/12D,,5/12] z/12 [D5/24D,,5/24] z/24 [C5/24C,,5/24] z/24 [D5/24D,,5/24] z/24 [D5/24D,,5/24] z/24 [D5/24D,,5/24] z/24 [d5/24D,5/24] z/24 [D11/24D,,11/24] z/24 [D5/24D,,5/24] z/24 [d5/24D,5/24] z/24 [D,,5/24D5/24] z/24\s
                    [c5/24A,,,5/12A,5/12C11/6E,11/6A,11/6] z/24 B5/24 [z/24c5/24] [A,,5/24A5/24c4/3] [z/24B5/24] [z5/24A,,,5/12A,5/12] [z7/24c4/3] [A,5/24A,,,5/24] z/24 [G,5/24G,,,5/24] z/24 [A,,,5/24A,5/24] z/24 [A,5/24A,,,5/24B17/24D11/6G,11/6B,11/6] z/24 [A,,,5/24A,5/24] [z/24B17/24] [A5/24A,,5/24] z/24 [A,,,11/24A,11/24c17/24] [z/24c17/24] [A,5/24A,,,5/24] z/24 [A5/24A,,5/24d11/24] z/24 [A,,,5/24A,5/24] [z/24d11/24]\s
                    [=F,5/12F,,,5/12E11/6A,11/6C11/6A23/6] z/24 [z/24A23/6] [=F5/24F,,5/24] z/24 [F,5/12F,,,5/12] z/12 [F,5/24F,,,5/24] z/24 [F5/24F,,5/24] z/24 [F,,,5/24F,5/24] z/24 [G,5/24G,,,5/24G,11/6B,11/6D11/6] z/24 [D5/24D,,5/24] z/24 [G5/24G,,5/24] z/24 [G,11/24G,,,11/24] z/24 [G5/24G,,5/24] z/24 [G,5/24G,,,5/24] z/24 [D,,5/24D5/24] z/24\s
                    [c5/24F,5/24F,,,5/24F11/6A,11/6C11/6] z/24 [B5/24F,,,5/24F,5/24] [z/24c5/24] [F,,5/24F5/24c4/3] [z/24B5/24] [F,,,5/24F,5/24] [z/24c4/3] [F,5/24F,,,5/24] z/24 [F5/24F,,5/24] z/24 [F,5/24F,,,5/24] z/24 [F,,,5/24F,5/24] z/24 [G,5/24G,,,5/24B17/24G11/6B,11/6D11/6] z/24 [G,,,5/24G,5/24] [z/24B17/24] [G5/24G,,5/24] z/24 [G,5/24G,,,5/24A17/24] z/24 [G,,,5/24G,5/24] [z/24A17/24] [G5/24G,,5/24] z/24 [G,5/24G,,,5/24G11/24] z/24 [G,,,5/24G,5/24] [z/24G11/24]\s
                    [A,5/24A,,,5/24A,11/6C11/6E11/6A23/6] z/24 [A,,,5/24A,5/24] [z/24A23/6] [A5/24A,,5/24] z/24 [A,5/24A,,,5/24] z/24 [G5/24G,,5/24] z/24 [A,5/24A,,,5/24] z/24 [A5/24A,,5/24] z/24 [A,,,5/24A,5/24] z/24 [G,5/24G,,,5/24G,11/6B,11/6D11/6] z/24 [G,5/24G,,,5/24] z/24 [G5/24G,,5/24] z/24 [G,5/24G,,,5/24] z/24 [F5/24F,,5/24] z/24 [G,5/24G,,,5/24] z/24 [G5/24G,,5/24] z/24 [G,,,5/24G,5/24] z/24\s
                    [c5/24F,5/24F,,,5/24C11/6F,11/6A,11/6] z/24 [B5/24F,,,5/24F,5/24] [z/24c5/24] [F,,5/24F5/24c4/3] [z/24B5/24] [F,,,5/24F,5/24] [z/24c4/3] [E5/24E,,5/24] z/24 [F,5/24F,,,5/24] z/24 [F5/24F,,5/24] z/24 [F,,,5/24F,5/24] z/24 [G,5/24G,,,5/24B17/24D11/6G,11/6B,11/6] z/24 [G,,,5/24G,5/24] [z/24B17/24] [G5/24G,,5/24] z/24 [G,5/24G,,,5/24c17/24] z/24 [F,,5/24F5/24] [z/24c17/24] [G,5/24G,,,5/24] z/24 [G5/24G,,5/24d11/24] z/24 [G,,,5/24G,5/24] [z/24d11/24]\s
                    [A,5/24A,,,5/24D11/6A23/6A,23/6E23/6] z/24 [A,,,5/24A,5/24] [z/24A7/] [A5/24A,,5/24] z/24 [A,5/24A,,,5/24] z/24 [E5/24E,,5/24] z/24 [A,5/24A,,,5/24] z/24 [A5/24A,,5/24] z/24 [A,,,5/24A,5/24] z/24 [A5/24A,,5/24^C11/6] z/24 [E5/24E,,5/24] z/24 [A,5/24A,,,5/24] z/24 [A5/24A,,5/24] z/24 [E5/24E,,5/24] z/24 [A,5/24A,,,5/24] z/24 [A5/24A,,5/24] z/24 [A,,,5/24A,5/24] z/24\s
                    [^F,,5/24^F,11/24^F,,,11/24E,23/6A,23/6C23/6] z/24 F,,5/24 z/24 [F,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [^F5/24F,,5/24F,5/24] z/24 [E5/24E,,5/24F,,5/24] z/24 [F5/24F,,5/24E,5/24] z/24 [F,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F,5/24F,,,5/24F,,5/24] z/24 [F,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F5/24F,,5/24E,5/24] z/24 [E5/24E,,5/24F,5/24] z/24 [F5/24F,,5/24F,,5/24] z/24 [^C,,5/24C5/24F,5/24] z/24\s
                    [F,,5/24F,11/24F,,,11/24E,23/6^G,23/6B,23/6] z/24 F,,5/24 z/24 [F,,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F5/24F,,5/24F,,5/24] z/24 [E5/24E,,5/24F,,5/24] z/24 [F5/24F,,5/24F,,5/24] z/24 [F,,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F,5/24F,,,5/24F,,5/24] z/24 [F,,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F5/24F,,5/24F,,5/24] z/24 [E5/24E,,5/24F,,5/24] z/24 [F5/24F,,5/24F,,5/24] z/24 [C,,5/24C5/24F,,5/24] z/24\s
                    [F,,5/24F,11/24F,,,11/24D,23/6F,23/6A,23/6] z/24 F,,5/24 z/24 [F,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F5/24F,,5/24F,5/24] z/24 [E5/24E,,5/24F,,5/24] z/24 [F5/24F,,5/24E,5/24] z/24 [F,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F,5/24F,,,5/24F,,5/24] z/24 [F,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F5/24F,,5/24E,5/24] z/24 [E5/24E,,5/24F,5/24] z/24 [F5/24F,,5/24F,,5/24] z/24 [C,,5/24C5/24F,5/24] z/24\s
                    [F,,5/24F,11/24F,,,11/24E,23/6G,23/6B,23/6] z/24 F,,5/24 z/24 [F,,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F5/24F,,5/24F,,5/24] z/24 [E5/24E,,5/24F,,5/24] z/24 [F5/24F,,5/24F,,5/24] z/24 [F,,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F,5/24F,,,5/24F,,5/24] z/24 [F,,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F5/24F,,5/24F,,5/24] z/24 [E5/24E,,5/24F,,5/24] z/24 [F5/24F,,5/24F,,5/24] z/24 [C,,5/24C5/24F,,5/24] z/24\s
                    [F,,5/24B/4F,11/24F,,,11/24E,23/6A,23/6] z/24 [F,,5/24c3/4] z/24 [F,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F5/24F,,5/24F,5/24^c17/6] z/24 [E5/24E,,5/24F,,5/24] z/24 [F5/24F,,5/24E,5/24] z/24 [F,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F,5/24F,,,5/24F,,5/24] z/24 [F,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F5/24F,,5/24E,5/24] z/24 [E5/24E,,5/24F,5/24] z/24 [F5/24F,,5/24F,,5/24] z/24 [C,,5/24C5/24F,5/24] z/24\s
                    [F,,5/24A/4F,11/24F,,,11/24E,23/6G,23/6] z/24 [F,,5/24_B3/4] z/24 [F,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F5/24F,,5/24F,5/24=B17/6] z/24 [E5/24E,,5/24F,,5/24] z/24 [F5/24F,,5/24E,5/24] z/24 [F,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F,5/24F,,,5/24F,,5/24] z/24 [F,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F5/24F,,5/24E,5/24] z/24 [E5/24E,,5/24F,5/24] z/24 [F5/24F,,5/24F,,5/24] z/24 [C,,5/24C5/24F,5/24] z/24\s
                    [F,,5/24G/4F,11/24F,,,11/24D,23/6F,23/6] z/24 [F,,5/24^G3/4] z/24 [F,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F5/24F,,5/24F,5/24A17/6] z/24 [E5/24E,,5/24F,,5/24] z/24 [F5/24F,,5/24E,5/24] z/24 [F,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F,5/24F,,,5/24F,,5/24] z/24 [F,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F5/24F,,5/24E,5/24] z/24 [E5/24E,,5/24F,5/24] z/24 [F5/24F,,5/24F,,5/24] z/24 [C,,5/24C5/24F,5/24] z/24\s
                    [F,,5/24A/4F,11/24F,,,11/24E,23/6G,23/6] z/24 [F,,5/24_B3/4] z/24 [F,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F5/24F,,5/24F,5/24=B17/6] z/24 [E5/24E,,5/24F,,5/24] z/24 [F5/24F,,5/24E,5/24] z/24 [F,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F,5/24F,,,5/24F,,5/24] z/24 [F,5/24F,11/24F,,,11/24] z/24 F,,5/24 z/24 [F5/24F,,5/24E,5/24] z/24 [E5/24E,,5/24F,5/24] z/24 [F5/24F,,5/24F,,5/24] z/24 [C,,5/24C5/24F,5/24]\s
                                        
                    """);
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

                songComponents = new SongComponent[components.size()];
                components.toArray(songComponents);
                pointer = 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void tick() {
        if (!playing) {
            return;
        }

        var ms = System.currentTimeMillis() - initial;
        // progress += client.getTickDelta();

        // System.out.println(progress);

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
        }
        // .player.sendMessage(Text.literal(String.valueOf(ms)), true);
    }

    public void playNote(SongNote note) {
        var noteIndex = note.getNote() - 24 + 6;
        var pitch = noteToPitch(note);
        var instrument = InstrumentRegistry.fromBlockState(blockEntity.getWorld().getBlockState(blockEntity.getPos().down()));

        var sound = instrument.getFromIndex(noteIndex);

        if (sound == null) {
            Log.warn(LogCategory.LOG, "Tried playing invalid note " + note.getRawNote());
            return;
        }

        if (canPlayNote(pitch)) {
            if (!note.isRest()) {
                if (blockEntity != null) {
                    blockEntity.getWorld().playSound(client.player, blockEntity.getPos(), sound, SoundCategory.RECORDS, 3, pitch);
                } else {
                    client.player.playSound(sound, 3, pitch);
                }
            }
        } else {
            // client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_SNARE, 1, 1);
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
        var noteIndex = note.getNote() - 24 + 6;
        var newNoteIndex = InstrumentRegistry.adjustIndex(noteIndex);

        float f = (float)Math.pow(2.0D, (double)(newNoteIndex) / 12.0D);
        // System.out.println(noteIndex);

        return f;
    }

    public boolean isPlaying() {
        return playing;
    }
}
