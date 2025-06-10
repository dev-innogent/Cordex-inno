package com.desktop.agent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CodeExecutor {

    public Path createFlaskApp(Path path) throws IOException {
        Files.createDirectories(path);
        Path appPy = path.resolve("app.py");
        String content = """
                from flask import Flask
                app = Flask(__name__)

                @app.route('/')
                def hello():
                    return 'Hello'

                if __name__ == '__main__':
                    app.run()
                """;
        Files.writeString(appPy, content);
        return appPy;
    }
}
