package me.stupidcat.abcparser;

import me.stupidcat.abcparser.utils.NumberUtil;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class ABCSong {
    SongMeter meter;
    int bpm;
    double defaultNoteLength;
    public double songProgress;

    Map<String, Integer> accidentalState;

    String keySignature;

    public ABCSong() {
        accidentalState = new HashMap<>();
    }

    public ABCSong setMeter(String meter) {
        this.meter = SongMeter.parse(meter);
        this.accidentalState = new HashMap<>();
        return this;
    }

    public ABCSong setKey(String key) {
        if (KeySignatures.KEY_SIGNATURES.containsKey(key)) {
            keySignature = key;
            resetAccidentalState();
        }
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

    public int getAccidentalState(String note) {
        note = note.toLowerCase();
        return accidentalState.getOrDefault(note, 0);
    }

    public void setAccidentalState(String note, int offset) {
        note = note.toLowerCase();
        accidentalState.put(note, offset);
    }

    public void resetAccidentalState() {
        accidentalState = KeySignatures.getKeySignature(keySignature);
    }
}
