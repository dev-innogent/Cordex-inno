# Cordex-inno

This repository contains experiments and documentation for Cordex's innovative solutions. More details will be added soon.

## Prerequisites

Building any of the Java modules requires **Maven** and a recent JDK. On Debian/Ubuntu systems you can install Maven with:

```bash
sudo apt-get update && sudo apt-get install -y maven
```

After installation verify it with `mvn -version` before running Maven commands like `mvn test`.

The `systemmind` module relies on the OpenRouter AI chat API. Set an `OPENROUTER_API_KEY` environment variable so it can request commands from the model. For quick testing you may use a key like `sk-or-example` but you should replace it with your own.

## Database

The Spring Boot project expects a local **PostgreSQL** database. By default it connects to `jdbc:postgresql://localhost:5432/db_local` using the `postgres` user with password `root`. Edit `src/main/resources/application.properties` if your environment uses different credentials.
