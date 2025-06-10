package com.systemmind;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class PermissionDialog {
    public static boolean ask(String description) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                description,
                ButtonType.OK, ButtonType.CANCEL);
        alert.setHeaderText("Approve Action?");
        var result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
