package com.starmediadev.plugins.starmcutils.messages;

public class Message {
    private final String message;

    public Message(String message) {
        this.message = message;
    }

    public String get(String... args) {
        String message = this.message;
        if (args != null) {
            if (args.length > 0) {
                if (args.length % 2 != 0) {
                    return message;
                }
                for (int i = 0; i < args.length; i += 2) {
                    message = message.replace(args[i], args[i + 1]);
                }
            }
        }
        return message;
    }
}
