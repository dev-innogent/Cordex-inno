# SystemMind AI

This directory contains a JavaFX prototype for the **SystemMind AI** desktop assistant. It now delegates natural language understanding to an online AI service instead of a local `CommandParser`. The assistant can optionally use voice input/output for confirmations.

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

Before running you must provide a Gemini API key via the `GEMINI_API_KEY` environment variable so `AICommandService` can contact the API. For example:

```bash
export GEMINI_API_KEY=AIzaSyC-Ne06g37yg2Cptgqhy9dw9jofYsYN2kk
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

- `MainUI` – JavaFX UI that interacts with the user and the AI service.
- `AICommandService` – queries the Gemini API to turn prompts into executable commands.
- `VoiceModule` – placeholders for speech recognition and text-to-speech.
- `SystemExecutor` – runs approved commands via `ProcessBuilder`.
- `Logger` – appends history entries to `systemmind.log`.

These classes are simplified placeholders to show the overall structure and require further work for a production-ready assistant.
