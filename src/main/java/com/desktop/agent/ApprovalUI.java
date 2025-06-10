package com.desktop.agent;

import javax.swing.*;

public class ApprovalUI {
    public boolean askApproval(String action) {
        int result = JOptionPane.showConfirmDialog(null,
                "Execute action: " + action + "?",
                "Approve Command",
                JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
}
