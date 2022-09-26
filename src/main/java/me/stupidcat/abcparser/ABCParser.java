package me.stupidcat.abcparser;

import me.stupidcat.abcparser.struct.SongChord;
import me.stupidcat.abcparser.struct.SongComponent;
import me.stupidcat.abcparser.struct.SongNote;

import java.util.ArrayList;
import java.util.List;

public class ABCParser {
    ABCSong songMeta;

    public ABCParser(ABCSong song) {
        this.songMeta = song;
    }

    public List<SongComponent> parse(String input) {
        input = input.replaceAll("\r", "");
        input = input.replaceAll("\n", " ");

        var parts = input.split(" ");

        var list = new ArrayList<SongComponent>();


        for (var part : parts) {
            var component = parsePart(part);
            list.add(component);
            songMeta.songProgress = component.getEnd();
        }

        return list;
    }

    public SongComponent parsePart(String input) {
        if (input.startsWith("[") && input.endsWith("]")) {
            return new SongChord(songMeta, input);
        } else {
            return new SongNote(songMeta, input);
        }
    }
}
