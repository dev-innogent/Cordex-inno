package com.systemmind;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class SystemExecutor {
    public static String execute(Command cmd) {
        ProcessBuilder pb = new ProcessBuilder(cmd.commandLine());
        pb.redirectErrorStream(true);
        StringBuilder out = new StringBuilder();
        try {
            Process p = pb.start();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    out.append(line).append("\n");
                }
            }
            p.waitFor();
        } catch (IOException | InterruptedException ex) {
            return "Failed: " + ex.getMessage();
        }
        return out.toString();
    }
}
