package com.starmediadev.plugins.starmcutils.util;

public final class TickUtils {
    private TickUtils(){}
    
    public static final String TIME_FORMAT = "h:mm a";
    
    public static long asMilliseconds(long duration) {
        return duration / 50;
    }

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
