# SystemMind AI

This directory contains a simple JavaFX skeleton for the **SystemMind AI** desktop assistant. It demonstrates how a Java application might parse commands, request user confirmation, and then execute system operations.

## Building

First, ensure **Maven** and a JDK are installed on your system. On Ubuntu/Debian
you can install Maven with:

```bash
sudo apt-get update && sudo apt-get install -y maven
```

Verify installation with `mvn -version`.

```bash
mvn package
```

Run the application with:

```bash
mvn javafx:run
```

To produce a platform-specific installer or executable, you can use `jpackage` after building the JAR. Example on Windows:

```bash
jpackage --input target --name SystemMind --main-jar systemmind-0.1.0-SNAPSHOT.jar --type exe
```

## Modules

- `MainUI` – basic JavaFX user interface.
- `CommandParser` – translates natural language text into commands.
- `PermissionDialog` – displays a confirmation dialog before execution.
- `SystemExecutor` – runs approved commands via `ProcessBuilder`.
- `Logger` – appends history entries to `systemmind.log`.

These classes are simplified placeholders to show the overall structure.
