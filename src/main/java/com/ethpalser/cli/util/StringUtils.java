package com.ethpalser.cli.util;

import java.util.Locale;

public class StringUtils {

    public static String capitalizeWord(String word) {
        if (word == null) {
            return null;
        }
        if (word.length() == 1) {
            return word.toUpperCase(Locale.ROOT);
        }
        String lower = word.toLowerCase(Locale.ROOT);
        return lower.substring(0, 1).toUpperCase(Locale.ROOT) + lower.substring(1);
    }

}
