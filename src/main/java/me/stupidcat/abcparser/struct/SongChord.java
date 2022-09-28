package me.stupidcat.abcparser.struct;

import me.stupidcat.abcparser.ABCSong;

import java.util.ArrayList;
import java.util.List;

public class SongChord extends SongComponent {
    public List<SongNote> notes;

    public SongChord(ABCSong song, String input) {
        super(song, input);
    }

    void parse(String input) {
        notes = new ArrayList<>();
        var match = SongNote.regex.matcher(input);

        while (match.find()) {
            var note = new SongNote(song, match.group("note"));
            notes.add(note);
        }
    }

    @Override
    public double getEnd() {
        if (notes.size() > 0) {
            double shortest = 0;
            for (var note : notes) {
                var end = note.getEnd();
                if (shortest == 0) {
                    shortest = end;
                } else if (shortest > end) {
                     shortest = end;
                }
            }

            return shortest;
        }
        return startMs;
    }
}
