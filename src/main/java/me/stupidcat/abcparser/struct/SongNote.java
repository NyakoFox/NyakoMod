package me.stupidcat.abcparser.struct;

import me.stupidcat.abcparser.ABCSong;

import java.util.regex.Pattern;

public class SongNote extends SongComponent {
    public static Pattern regex = Pattern.compile("(?<note>(?<prefix>[_=^]?)(?<noteName>[za-gA-G])(?<octave>[',]*)(?<num>\\d*)(?<dem>(/\\d+)?))");

    String rawNote;
    String noteName;
    int note;
    double lengthMultiplier;
    boolean rest;

    public SongNote(ABCSong song, String input) {
        super(song, input);
    }

    void parse(String input) {
        var match = regex.matcher(input);
        if (match.find()) {
            rawNote = input;
            var prefix = match.group("prefix");
            noteName = match.group("noteName");
            var octave = match.group("octave");
            var num = match.group("num");
            var dem = match.group("dem");

            int offset = 0;
            if (prefix.equals("^")) {
                offset = 1;
            } else if (prefix.equals("_")) {
                offset = -1;
            }

            for (var c : octave.toCharArray()) {
                if (c == '\'') {
                    offset += 12;
                } else if (c == ',') {
                    offset -= 12;
                }
            }

            int numI = 1;
            int demI = 1;

            if (!num.isBlank()) {
                numI = Integer.parseInt(num);
            }
            if (!dem.isBlank()) {
                demI = Integer.parseInt(dem.substring(1));
            }

            lengthMultiplier = (double) numI / (double) demI;

            int value = getNoteValue(noteName.charAt(0));
            note = value + offset;

            rest = noteName.equals("z") || noteName.equals("x");
        }
    }

    @Override
    public double getEnd() {
        return startMs + song.getModifiedNoteLength(lengthMultiplier);
    }

    int getNoteValue(char note) {
        return switch (note) {
            case 'D' -> 2;
            case 'E' -> 4;
            case 'F' -> 5;
            case 'G' -> 7;
            case 'A' -> 9;
            case 'B' -> 11;
            case 'c' -> 12;
            case 'd' -> 2 + 12;
            case 'e' -> 4 + 12;
            case 'f' -> 5 + 12;
            case 'g' -> 7 + 12;
            case 'a' -> 9 + 12;
            case 'b' -> 11 + 12;
            default -> 0;
        };
    }

    public int getNote() {
        return note;
    }

    public String getRawNote() {
        return rawNote;
    }

    public boolean isRest() {
        return rest;
    }
}
