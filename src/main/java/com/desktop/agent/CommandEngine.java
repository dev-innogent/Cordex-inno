package com.desktop.agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandEngine {

    public String execute(String action) {
        try {
            if (action.startsWith("install:")) {
                String pkg = action.split(":", 2)[1];
                return installPackage(pkg);
            }
            if (action.startsWith("open:")) {
                String app = action.split(":", 2)[1];
                return openApp(app);
            }
            if ("create_project".equals(action)) {
                return "Project creation not implemented.";
            }
            if ("clear_temp".equals(action)) {
                return clearTemp();
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Unknown action";
    }

    private String installPackage(String pkg) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("powershell", "-Command", "winget install --silent " + pkg);
        Process p = pb.start();
        return readOutput(p);
    }

    private String openApp(String app) throws Exception {
        new ProcessBuilder(app).start();
        return "Opened " + app;
    }

    private String clearTemp() throws Exception {
        Process p = new ProcessBuilder("powershell", "-Command", "Remove-Item -Path $env:TEMP\\* -Recurse -Force").start();
        return readOutput(p);
    }

    private String readOutput(Process p) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append(System.lineSeparator());
        }
        p.waitFor();
        return sb.toString();
    }
}
