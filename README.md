# Cordex-inno

This repository contains experiments and documentation for Cordex's innovative solutions. It now includes a prototype Windows desktop AI agent written in Java.

## Windows Desktop Agent

A simple Swing application allows you to enter natural language commands which are parsed and, upon your approval, executed using PowerShell.

### Build and Run

```
# compile the project
mvn package

# run the desktop agent
java -cp target/classes com.desktop.agent.AgentApp
```

The agent currently supports very basic commands such as installing software via `winget`, opening applications, and clearing temporary files. It shows a confirmation dialog before performing any action.

### Build Windows Executable

To package the agent as a Windows `.exe` you need JDK 17+ with `jpackage` available. Run the provided PowerShell script:

```
pwsh scripts/build_win_exe.ps1
```

This generates a native installer in the `dist` folder.
