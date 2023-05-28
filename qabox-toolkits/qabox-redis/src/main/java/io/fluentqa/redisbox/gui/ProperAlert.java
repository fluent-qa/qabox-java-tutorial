package io.fluentqa.redisbox.gui;

import io.fluentqa.redisbox.gui.util.GuiUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class ProperAlert extends Alert {
    private final static double MAX_WIDTH = 500;
    public ProperAlert(AlertType alertType) {
        super(alertType);
        ((Stage) getDialogPane().getScene().getWindow()).getIcons().add(App.icon);
        getDialogPane().getStylesheets().add(GuiUtils.getResourcePath("css/button.css"));
        getDialogPane().setMaxWidth(MAX_WIDTH);
    }

    public ProperAlert(AlertType alertType, String contentText, ButtonType... buttons) {
        super(alertType, contentText, buttons);
        ((Stage) getDialogPane().getScene().getWindow()).getIcons().add(App.icon);
        getDialogPane().getStylesheets().add(GuiUtils.getResourcePath("css/button.css"));
        getDialogPane().setMaxWidth(MAX_WIDTH);
    }
}
