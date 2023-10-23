package me.stupidcat.abcparser.struct;

import me.stupidcat.abcparser.ABCSong;

public abstract class SongComponent {
    double startMs;
    double endMs;
    ABCSong song;

    public SongComponent(ABCSong song, String input) {
        this.song = song;
        startMs = song.songProgress;
        parse(input);
    }

    abstract void parse(String input);
    public abstract double getEnd();

    public double getStart() {
        return startMs;
    }
}
