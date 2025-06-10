package com.desktop.agent;

public class SoftwareInstaller {
    public String install(String packageName) throws Exception {
        CommandEngine engine = new CommandEngine();
        return engine.execute("install:" + packageName);
    }
}
