package com.systemmind;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {
    private final String path = "systemmind.log";

    public void log(String message) {
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(LocalDateTime.now() + " : " + message + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
