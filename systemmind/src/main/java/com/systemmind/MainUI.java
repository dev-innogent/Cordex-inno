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
        input.setPromptText("Enter command...");
        TextArea output = new TextArea();
        output.setEditable(false);

        Button run = new Button("Execute");
        run.setOnAction(e -> handleCommand(input.getText(), output));

        BorderPane root = new BorderPane(input);
        root.setBottom(run);
        root.setRight(output);
        stage.setTitle("SystemMind AI");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    private void handleCommand(String text, TextArea outputArea) {
        Command cmd = CommandParser.parse(text);
        if (cmd == null) {
            String msg = "Could not understand: " + text;
            outputArea.appendText(msg + System.lineSeparator());
            logger.log(msg);
            return;
        }
        boolean approved = PermissionDialog.ask(cmd.description());
        if (approved) {
            String result = SystemExecutor.execute(cmd);
            logger.log(cmd.description() + " => " + result);
            outputArea.appendText(result + System.lineSeparator());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
