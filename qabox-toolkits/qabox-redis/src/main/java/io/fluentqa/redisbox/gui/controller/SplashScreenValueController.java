package io.fluentqa.redisbox.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

public class SplashScreenValueController {
    @FXML
    private ProgressBar progressBar;

    public void setProgress(double v) {
        progressBar.setProgress(v);
    }
}
