package com.desktop.agent;

public class PromptHandler {
    public String parse(String text) {
        String t = text.toLowerCase();
        if (t.contains("install")) {
            return "install:" + t.split("install", 2)[1].trim();
        }
        if (t.contains("open")) {
            return "open:" + t.split("open", 2)[1].trim();
        }
        if (t.contains("create") && t.contains("project")) {
            return "create_project";
        }
        if (t.contains("clear") && t.contains("temp")) {
            return "clear_temp";
        }
        return null;
    }
}
