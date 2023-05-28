package io.fluentqa.redisbox.gui.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.fluentqa.redisbox.base.Language;
import io.fluentqa.redisbox.base.StringUtils;
import io.fluentqa.redisbox.gui.ProperAlert;
import io.fluentqa.redisbox.gui.buttontype.MyButtonType;
import io.fluentqa.redisbox.gui.util.GuiUtils;
import io.fluentqa.redisbox.service.RedisConnection;
import io.fluentqa.redisbox.service.RedisConnector;
import io.fluentqa.redisbox.service.client.RedisClient;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class RedisConnectionEditController {

    private final ImageView loadingImageView;

    FontAwesomeIconView okIcon =
            new FontAwesomeIconView(FontAwesomeIcon.CHECK);
    FontAwesomeIconView warnIcon =
            new FontAwesomeIconView(FontAwesomeIcon.WARNING);
    FontAwesomeIconView linkIcon =
            new FontAwesomeIconView(FontAwesomeIcon.LINK);
    private Stage stage;
    private ObservableList<RedisConnection> connectionList;
    private RedisConnection originalItem;

    public void initialize(Stage stage, ObservableList<RedisConnection> connectionList, RedisConnection configuration, RedisConnection originalItem) {
        this.redisConnection = configuration;
        this.connectionList = connectionList;
        this.originalItem = originalItem;
        this.stage = stage;
        nameTextField.setText(configuration.getName());
        hostTextField.setText(configuration.getHost());
        portTextField.setText(String.valueOf(configuration.getPort()));
        authTextField.setText(configuration.getAuth());

        nameTextField.textProperty().addListener(this::invalidated);
        hostTextField.textProperty().addListener(this::invalidated);
        portTextField.textProperty().addListener(this::invalidated);
        authTextField.textProperty().addListener(this::invalidated);
    }

    private RedisConnection redisConnection;

    public void setRedisConnection(RedisConnection redisConnection) {
        this.redisConnection = redisConnection;
    }

    public RedisConnection getRedisConnection() {
        return redisConnection;
    }

    public RedisConnectionEditController() {
        Image loadingImage = new Image(getClass().getResourceAsStream("/images/loading.gif"));
        loadingImageView = new ImageView(loadingImage);
        Image okImage = new Image(getClass().getResourceAsStream("/images/ok.png"));
        Image warnImage = new Image(getClass().getResourceAsStream("/images/warn.png"));
    }

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField hostTextField;
    @FXML
    private TextField portTextField;
    @FXML
    private TextField authTextField;
    @FXML
    private Button testButton;

    @FXML
    private Label warningLabel;

    @FXML
    private void onTextFieldChanged() {
        testButton.setGraphic(linkIcon);
    }

    private void setTextFieldDisabled(boolean disabled) {
        authTextField.setDisable(disabled);
        hostTextField.setDisable(disabled);
        portTextField.setDisable(disabled);
    }

    @FXML
    private void testConnection() {
        initImageViewSize();
        testButton.setGraphic(loadingImageView);
        AtomicBoolean success = new AtomicBoolean(false);
        AtomicReference<String> message = new AtomicReference<>("");
        setTextFieldDisabled(true);
        new Thread(() -> {
            RedisConnector connector = new RedisConnector(redisConnection);
            RedisClient client = connector.connect(success, message);
            try {
                if (client != null) {
                    client.close();
                }
            } catch (IOException ignored) {
            }
            Platform.runLater(() -> {
                if (success.get()) {
                    testButton.setGraphic(okIcon);
                } else {
                    testButton.setGraphic(warnIcon);
                }
                warningLabel.setText(message.get());
                setTextFieldDisabled(false);
            });
        }).start();
    }


    @FXML
    private void close() {
        stage.close();
    }

    private boolean isSaved = false;

    public boolean isSaved() {
        return isSaved;
    }

    @FXML
    private void saveAndClose() {
        if (StringUtils.isBlank(redisConnection.getName())) {
            redisConnection.setName(String.format("%s:%d", redisConnection.getHost(), redisConnection.getPort()));
        }
        String name = redisConnection.getName();
        boolean isUnique = isUnique(name);
        if (isUnique) {
            isSaved = true;
            close();
        } else {
            Alert alert = new ProperAlert(Alert.AlertType.ERROR);
            alert.getButtonTypes().setAll(MyButtonType.OK);
            alert.setTitle(Language.getString("redis_connection_name_exist"));
            alert.setHeaderText(String.format(Language.getString("redis_connection_name_exist_text"), redisConnection.getName()));
            alert.getDialogPane().getStylesheets().add(GuiUtils.getResourcePath("css/button.css"));
            alert.show();
        }
    }

    private boolean isUnique(String name) {
        for (int i = 0; i < connectionList.size(); i++) {
            RedisConnection connection = connectionList.get(i);
            if (connection == originalItem) {
                continue;
            }
            if (name.equals(connection.getName())) {
                return false;
            }
        }
        return true;
    }

    private void initImageViewSize() {
        double fontSize = testButton.getFont().getSize();
        loadingImageView.setFitHeight(fontSize);
        loadingImageView.setFitWidth(fontSize);
    }

    private void invalidated(Observable e) {
        redisConnection.setName(nameTextField.getText());
        redisConnection.setHost(hostTextField.getText());
        redisConnection.setPort(Integer.parseInt(portTextField.getText()));
        redisConnection.setAuth(authTextField.getText());
    }

    public void keyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            close();
        }
    }
}
