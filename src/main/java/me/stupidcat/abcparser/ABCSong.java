package me.stupidcat.abcparser;

import me.stupidcat.abcparser.utils.NumberUtil;

public class ABCSong {
    SongMeter meter;
    int bpm;
    double defaultNoteLength;
    public double songProgress;

    public ABCSong setMeter(String meter) {
        this.meter = SongMeter.parse(meter);
        return this;
    }

    public ABCSong setBpm(String bpm) {
        this.bpm = Integer.parseInt(bpm);
        return this;
    }

    public ABCSong setBpm(int bpm) {
        this.bpm = bpm;
        return this;
    }

    public ABCSong setDefaultNoteLength(String length) {
        this.defaultNoteLength = NumberUtil.parseFraction(length);
        return this;
    }

    public ABCSong setDefaultNoteLength(double length) {
        this.defaultNoteLength = length;
        return this;
    }

    public double getMSPerBeat() {
        return 60000d / bpm;
    }

    public double getModifiedNoteLength(double modifier) {
        return (getMSPerBeat() * meter.numerator) * defaultNoteLength * modifier;
    }
}
