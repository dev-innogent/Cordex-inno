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
        } else if (text.contains("time format") && text.contains("12")) {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                String[] cmd = {"cmd", "/c",
                        "reg", "add",
                        "HKCU\\Control Panel\\International",
                        "/v", "sShortTime",
                        "/t", "REG_SZ",
                        "/d", "h:mm tt",
                        "/f"};
                return new Command("Set time format to 12h", cmd);
            } else {
                return new Command("Set time format to 12h (unsupported OS)", new String[]{"echo", "unsupported"});
            }
        }
        // Add more patterns here
        return null;
    }
}
