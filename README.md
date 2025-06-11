# Cordex-inno

This repository contains experiments and documentation for Cordex's innovative solutions. More details will be added soon.

## Prerequisites

Building any of the Java modules requires **Maven** and a recent JDK. On Debian/Ubuntu systems you can install Maven with:

```bash
sudo apt-get update && sudo apt-get install -y maven
```

After installation verify it with `mvn -version` before running Maven commands like `mvn test`.

The `systemmind` module integrates with an online Google AI service. Set a `GEMINI_API_KEY` environment variable before running it so the application can contact the API. It uses the free `chat-bison-001` model instead of `gemini-pro` to avoid access errors. If you want to test quickly you can use the example key `AIzaSyC-Ne06g37yg2Cptgqhy9dw9jofYsYN2kk`.

## Database

The Spring Boot project expects a local **PostgreSQL** database. By default it connects to `jdbc:postgresql://localhost:5432/db_local` using the `postgres` user with password `root`. Edit `src/main/resources/application.properties` if your environment uses different credentials.
