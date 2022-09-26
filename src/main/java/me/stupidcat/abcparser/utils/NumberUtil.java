package me.stupidcat.abcparser.utils;

public class NumberUtil {
    public static double parseFraction(String input) {
        var parts = input.split("/");

        var numerator = Double.parseDouble(parts[0]);
        var denominator = Double.parseDouble(parts[0]);

        return numerator / denominator;
    }
}
