package io.fluentqa.redisbox.gui.component;

import io.fluentqa.redisbox.base.Language;
import io.fluentqa.redisbox.gui.JsonCodeField;
import io.fluentqa.redisbox.gui.util.GuiUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ValueEditorPane extends VBox {
    public JsonCodeField valueCodeArea;
    public Button formatButton;
    public Button minButton;
    public ChoiceBox<String> typeChoiceBox;
    private StringProperty value = new SimpleStringProperty(null);
    private StringProperty editorValue = new SimpleStringProperty(null);

    public ValueEditorPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(GuiUtils.getResourceURL("fxml/component/value-editor.fxml"), Language.getBundle());
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        formatButton.setOnAction(event -> valueCodeArea.format());
        minButton.setOnAction(event -> valueCodeArea.minify());
        typeChoiceBox.getItems().addAll(
                Language.getString("redis_json"),
                Language.getString("redis_plain_text")
        );
        typeChoiceBox.setValue(Language.getString("redis_json"));
        valueCodeArea.textProperty().addListener(listener -> {
            editorValue.set(valueCodeArea.getText());
        });
    }

    public void modeChange() {
        String typeValue = typeChoiceBox.getValue();
        boolean json = "JSON".equals(typeValue);
        valueCodeArea.setHighlight(json);
        formatButton.setVisible(json);
        minButton.setVisible(json);
    }

    private boolean isJson(String text) {
        return text.startsWith("{") || text.startsWith("[");
    }

    public void setText(String text) {
        if(text == null) {
            text = "";
        }
        if (isJson(text)) {
            typeChoiceBox.setValue("JSON");
        }
        modeChange();
        valueCodeArea.replaceText(text);
        this.value.set(text);
    }

    public String getText() {
        return valueCodeArea.getText();
    }

    public ObservableValue<String> textProperty() {
        return valueCodeArea.textProperty();
    }

    public void clear() {
        valueCodeArea.clear();
        typeChoiceBox.setValue(Language.getString("redis_json"));
    }

    public boolean isSaved() {
        return this.value.getValue().equals(valueCodeArea.getText());
    }

    public ObservableBooleanValue isSavedBinding() {
        return editorValue.isEqualTo(value);
    }

    public void save() {
        value.set(getText());
    }

    public void restore() {
        setText(value.getValue());
    }
}
