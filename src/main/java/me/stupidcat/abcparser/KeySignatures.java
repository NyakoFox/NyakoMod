package me.stupidcat.abcparser;

import java.util.HashMap;
import java.util.Map;

public class KeySignatures {
    public static final Map<String, Map<String, Integer>> KEY_SIGNATURES;

    static {
        KEY_SIGNATURES = new HashMap<>();

        registerSignature("C", "Am");
        registerSignature("F", "Dm", "Bb");
        registerSignature("Bb", "Gm", "Bb", "Eb");
        registerSignature("Eb", "Cm", "Bb", "Eb", "Ab");
        registerSignature("Ab", "Fm", "Bb", "Eb", "Ab", "Db");
        registerSignature("Db", "Bbm", "Bb", "Eb", "Ab", "Db", "Gb");
        registerSignature("Gb", "Ebm", "Bb", "Eb", "Ab", "Db", "Gb", "Cb");
        registerSignature("Cb", "Abm", "Bb", "Eb", "Ab", "Db", "Gb", "Cb", "Fb");

        registerSignature("G", "Em", "F#");
        registerSignature("D", "Bm", "F#", "C#");
        registerSignature("A", "F#m", "F#", "C#", "G#");
        registerSignature("E", "C#m", "F#", "C#", "G#", "D#");
        registerSignature("B", "G#m", "F#", "C#", "G#", "D#", "A#");
        registerSignature("F#", "D#m", "F#", "C#", "G#", "D#", "A#", "E#");
        registerSignature("C#", "A#m", "F#", "C#", "G#", "D#", "A#", "E#", "B#");
    }

    static void registerSignature(String major, String minor, String... notes) {
        Map<String, Integer> list = new HashMap<>();

        for (var note : notes) {
            var offset = 0;
            if (note.endsWith("#")) {
                offset = 1;
            } else if (note.endsWith("b")) {
                offset = -1;
            }

            if (offset != 0) {
                list.put(note.substring(0, 1).toLowerCase(), offset);
            }
        }

        KEY_SIGNATURES.put(major, list);
        KEY_SIGNATURES.put(minor, list);
    }

    public static Map<String, Integer> getKeySignature(String key) {
        if (KEY_SIGNATURES.containsKey(key)) {
            var keySig = new HashMap<>(KEY_SIGNATURES.get(key));
            return keySig;
        } else {
            return new HashMap<>();
        }
    }
}
