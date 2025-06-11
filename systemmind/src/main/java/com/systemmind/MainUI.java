package com.systemmind;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

// client for OpenRouter AI
import com.systemmind.AIClient;

public class MainUI extends Application {
    private final Logger logger = new Logger();
    private final VoiceModule voice = new VoiceModule();
    private AIClient ai;

    @Override
    public void start(Stage stage) {
        ai = new AIClient(System.getenv("OPENROUTER_API_KEY"));

        TextArea input = new TextArea();
        input.setPromptText("Enter command...");
        TextArea output = new TextArea();
        output.setEditable(false);

        Button run = new Button("Execute");
        run.setOnAction(e -> handleCommand(input.getText(), output));

        Button voiceBtn = new Button("Voice");
        voiceBtn.setOnAction(e -> {
            String spoken = voice.listen();
            if (spoken != null) {
                input.setText(spoken);
                handleCommand(spoken, output);
            }
        });

        BorderPane root = new BorderPane(input);
        root.setBottom(run);
        root.setLeft(voiceBtn);
        root.setRight(output);
        stage.setTitle("SystemMind AI");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    private void handleCommand(String text, TextArea outputArea) {
        Command cmd;
        try {
            cmd = ai.requestCommand(text);
        } catch (Exception ex) {
            String msg = "AI error: " + ex.getMessage();
            outputArea.appendText(msg + System.lineSeparator());
            logger.log(msg);
            return;
        }

        if (cmd == null) {
            String msg = "No command returned";
            outputArea.appendText(msg + System.lineSeparator());
            logger.log(msg);
            return;
        }

        boolean approved = voice.askYesNo("Execute: " + cmd.description() + "?");
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
