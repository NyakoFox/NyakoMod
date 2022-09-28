package me.stupidcat.abcparser;

import me.stupidcat.abcparser.struct.SongChord;
import me.stupidcat.abcparser.struct.SongComponent;
import me.stupidcat.abcparser.struct.SongNote;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ABCParser {
    public static Pattern metaRegex = Pattern.compile("^(?<key>[A-Z]): ?(?<value>.+?)(=(?<extra>.+))?$");

    ABCSong songMeta;

    public ABCParser(ABCSong song) {
        this.songMeta = song;
    }

    public List<SongComponent> parse(String input) {
        try {
            input = input.replaceAll("\r", "");

            var lines = input.trim().split("\n");

            var list = new ArrayList<SongComponent>();

            if (!lines[0].startsWith("X:")) {
                return new ArrayList<>();
            }

            for (var line : lines) {
                if (line.trim().isBlank()) {
                    continue;
                }
                var match = metaRegex.matcher(line);
                if (match.find()) {
                    var key = match.group("key");
                    var value = match.group("value");
                    var extra = match.group("extra");

                    switch (key) {
                        case "L" -> songMeta.setDefaultNoteLength(value);
                        case "Q" -> {
                            var meter = SongMeter.parse(value);
                            if (extra != null) {
                                songMeta.setBpm(Integer.parseInt(extra) * meter.denominator);
                            }
                        }
                        case "K" -> songMeta.setKey(value);
                        case "M" -> songMeta.setMeter(value);
                    }

                    continue;
                }

                var parts = line.trim().split("\s+");
                for (var part : parts) {
                    var component = parsePart(part);
                    list.add(component);
                    songMeta.songProgress = component.getEnd();
                }
            }
            return list;

        } catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public SongComponent parsePart(String input) {
        if (input.startsWith("[") && input.endsWith("]")) {
            return new SongChord(songMeta, input);
        } else {
            return new SongNote(songMeta, input);
        }
    }
}
