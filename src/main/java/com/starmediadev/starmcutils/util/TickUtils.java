package com.starmediadev.starmcutils.util;

public final class TickUtils {
    private TickUtils(){}

    public static int asSeconds(int duration) {
        return duration * 20;
    }

    public static int asMinutes(int duration) {
        return asSeconds(1) * 60 * duration;
    }
    
    public static int asHours(int duration) {
        return asMinutes(1) * 60 * duration;
    }
}
