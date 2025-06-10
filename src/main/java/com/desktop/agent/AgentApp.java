package com.desktop.agent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AgentApp extends JFrame {

    private JTextField promptEntry;
    private JTextArea logArea;

    private final PromptHandler promptHandler = new PromptHandler();
    private final CommandEngine commandEngine = new CommandEngine();
    private final ApprovalUI approvalUI = new ApprovalUI();

    public AgentApp() {
        setTitle("Desktop AI Agent");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        createWidgets();
    }

    private void createWidgets() {
        setLayout(new BorderLayout());
        promptEntry = new JTextField();
        JButton runButton = new JButton("Run");
        runButton.addActionListener(this::handlePrompt);
        JPanel top = new JPanel(new BorderLayout());
        top.add(promptEntry, BorderLayout.CENTER);
        top.add(runButton, BorderLayout.EAST);

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(logArea);

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private void handlePrompt(ActionEvent e) {
        String text = promptEntry.getText().trim();
        if (text.isEmpty()) {
            return;
        }
        String action = promptHandler.parse(text);
        if (action == null) {
            log("Could not understand the command.");
            return;
        }
        if (approvalUI.askApproval(action)) {
            String output = commandEngine.execute(action);
            log(output);
        } else {
            log("Denied command: " + action);
        }
    }

    private void log(String msg) {
        logArea.append(msg + System.lineSeparator());
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AgentApp app = new AgentApp();
            app.setVisible(true);
        });
    }
}
