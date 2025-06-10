package com.systemmind;

/** Very naive parser that recognizes a few sample commands. */
public class CommandParser {
    public static Command parse(String text) {
        text = text.toLowerCase().trim();
        if (text.startsWith("create file")) {
            String name = text.substring("create file".length()).trim();
            if (name.isEmpty()) return null;
            return new Command("Create file " + name,
                    new String[]{"cmd", "/c", "type nul > " + name});
        } else if (text.startsWith("list dir")) {
            return new Command("List directory", new String[]{"cmd", "/c", "dir"});
        }
        // Add more patterns here
        return null;
    }
}
