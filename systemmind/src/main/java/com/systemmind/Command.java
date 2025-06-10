package com.systemmind;

/** Simple representation of a parsed command. */
public record Command(String description, String[] commandLine) {
}
