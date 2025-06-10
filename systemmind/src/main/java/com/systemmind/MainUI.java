package com.systemmind;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainUI extends Application {
    private final Logger logger = new Logger();

    @Override
    public void start(Stage stage) {
        TextArea input = new TextArea();
        Button run = new Button("Execute");
        run.setOnAction(e -> handleCommand(input.getText()));
        BorderPane root = new BorderPane(input);
        root.setBottom(run);
        stage.setTitle("SystemMind AI");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    private void handleCommand(String text) {
        Command cmd = CommandParser.parse(text);
        if (cmd == null) {
            logger.log("Could not understand: " + text);
            return;
        }
        boolean approved = PermissionDialog.ask(cmd.description());
        if (approved) {
            String result = SystemExecutor.execute(cmd);
            logger.log(cmd.description() + " => " + result);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
