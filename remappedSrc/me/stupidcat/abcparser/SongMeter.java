package me.stupidcat.abcparser;

import ;
import I;

public class SongMeter {
    public int numerator;
    public int denominator;

    public SongMeter(int numerator, int denominator) {
        this.denominator = denominator;
        this.numerator = numerator;
    }

    public static SongMeter parse(String input) {
        input = input.toLowerCase();
        if (input.equals("c")) {
            return new SongMeter(4, 4);
        } else if (input.equals("c|")) {
            return new SongMeter(2, 2);
        }

        var parts = input.split("/");

        var num = Integer.parseInt(parts[0]);
        var dem = Integer.parseInt(parts[1]);

        return new SongMeter(num, dem);
    }
}
