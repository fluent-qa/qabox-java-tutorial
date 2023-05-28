package io.fluentqa.redisbox.gui.controller.valueeditor;

import io.fluentqa.redisbox.gui.AdaptiveSplitPane;
import io.fluentqa.redisbox.gui.util.GuiUtils;
import io.fluentqa.redisbox.service.protocol.RespArray;
import io.fluentqa.redisbox.service.protocol.RespString;
import javafx.beans.InvalidationListener;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import io.fluentqa.redisbox.base.Language;
import io.fluentqa.redisbox.base.StringUtils;
import io.fluentqa.redisbox.gui.ProperAlert;
import io.fluentqa.redisbox.gui.buttontype.MyButtonType;
import io.fluentqa.redisbox.gui.component.*;
import io.fluentqa.redisbox.gui.controller.tabs.DataViewTabController;
import io.fluentqa.redisbox.service.RedisConnection;
import io.fluentqa.redisbox.service.client.RedisClient;
import io.fluentqa.redisbox.service.client.RespException;
import javafx.beans.property.StringProperty;
public class RedisZSetViewController implements RedisValueViewController {
    public NameEditorPane nameEditorPane;

    public TableView<Map<String, String>> valueListView;
    private final ObservableList<Map<String, String>> valueList = FXCollections.observableList(new ArrayList<>());

    public TableColumn<Map<String, String>, String> indexColumn;
    public TableColumn<Map<String, String>, String> valueColumn;
    public TableColumn<Map<String, String>, String> scoreColumn;

    public MenuItem removeMenuItem;
    public AdaptiveSplitPane splitPane;
    public ValueEditorPane valueEdit;
    public TTLEditorPane ttlEditor;
    public TextField scoreTextField;
    private RedisClient client;

    private Integer selectedIndex = null;
    private DataViewTabController dataViewTabController;
    private boolean isNew = false;

    private void init(StringProperty key) {
        valueListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        splitPane.hide();
        valueListView.setItems(valueList);
        valueListView.getStylesheets().add(GuiUtils.getResourcePath("css/list-editor.css"));
        valueListView.getSelectionModel().getSelectedIndices().addListener((InvalidationListener) listener -> {
            boolean empty = valueListView.getSelectionModel().getSelectedIndices().isEmpty();
            removeMenuItem.setDisable(empty);

            selectedIndex = valueListView.getSelectionModel().getSelectedIndex();
            if (valueListView.getSelectionModel().getSelectedIndices().size() == 0 || valueListView.getSelectionModel().getSelectedIndices().size() > 1) {
                return;
            }
            splitPane.show();
            if (valueListView.getSelectionModel().getSelectedItem() != null) {
                Map<String, String> selected = valueListView.getSelectionModel().getSelectedItem();
                scoreTextField.setText(selected.get("score"));
                valueEdit.setText(selected.get("value"));
            }
        });
        ttlEditor.init(client, key);
        nameEditorPane.init(client, key, dataViewTabController);
        valueColumn.setCellFactory(TableValueFactory.factory);
    }

    @Override
    public void initEdit(RedisConnection connection, int dbIndex, StringProperty key, DataViewTabController dataViewTabController) {
        client = connection.getClient();
        this.dataViewTabController = dataViewTabController;
        init(key);
        refreshValueList();
    }

    public void refreshValueList() {
        try {
            loadListValue(nameEditorPane.getCurrentKey(), client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadListValue(String key, RedisClient client) throws IOException {
        RespArray<RespString> values = client.zrange(key);
        valueList.clear();
        if (values == null) {
            return;
        }
        for (int i = 0; i < values.size(); i += 2) {
            String k = values.get(i).getValue();
            String score = values.get(i + 1).getValue();
            Map<String, String> kv = Map.of("value", k, "score", score, "index", String.valueOf(i / 2 + 1));
            valueList.add(kv);
        }
    }

    @Override
    public void initNew(RedisConnection connection, int dbIndex, StringProperty key, Stage stage, DataViewTabController dataViewTabController) {
        this.dataViewTabController = dataViewTabController;
        init(key);
        client = connection.getClient();
        isNew = true;
    }

    @Override
    public void restore() {
        nameEditorPane.restore();
        ttlEditor.restore();
        valueEdit.restore();
    }


    public void remove() {
        List<Map<String, String>> selectedItems = valueListView.getSelectionModel().getSelectedItems();
        Alert alert = new ProperAlert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(MyButtonType.YES, MyButtonType.NO);
        alert.setTitle(Language.getString("redis_delete_confirmation"));
        String name = valueListView.getSelectionModel().getSelectedItem().get("value");

        if (selectedItems.size() == 1) {
            alert.setHeaderText(String.format(Language.getString("redis_delete_name"), name));
        } else {
            alert.setHeaderText(String.format(Language.getString("redis_delete_count"), selectedItems.size()));
        }
        alert.getDialogPane().getStylesheets().add(GuiUtils.getResourcePath("css/button.css"));
        alert.showAndWait().ifPresent(type -> {
                    String key = nameEditorPane.getCurrentKey();
                    if (type == MyButtonType.YES) {
                        try {
                            if (selectedItems.size() == 1) {
                                client.zrem(key, selectedItems.get(0).get("value"));
                            } else {
                                for (Map<String, String> item : selectedItems) {
                                    client.zrem(key, item.get("value"));
                                }
                            }
                            loadListValue(key, client);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    public void edit() {
        var selectionModel = valueListView.getSelectionModel();
        if (!selectionModel.getSelectedIndices().isEmpty()) {
            int selectedIndex = selectionModel.getSelectedIndex();
        }
    }


    public void cancel() {
        splitPane.hide();
        valueListView.getSelectionModel().clearSelection();
        selectedIndex = null;
    }

    public void newValue() {
        splitPane.show();
        valueEdit.clear();
        scoreTextField.clear();
        valueListView.getSelectionModel().clearSelection();
        selectedIndex = null;
    }


    public boolean saveValue() throws IOException {
        String newValue = valueEdit.getText();
        String key = nameEditorPane.getCurrentKey();

        if(StringUtils.isEmpty(key)) {
            Alert alert = new ProperAlert(Alert.AlertType.ERROR);
            alert.getButtonTypes().setAll(MyButtonType.OK);
            alert.setHeaderText(Language.getString("redis_alert_key_empty"));
            alert.showAndWait();
            return false;
        }
        Map<String, String> selectedItem = valueListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            client.zrem(key, selectedItem.get("value"));
        }
        client.zadd(key, scoreTextField.getText(), newValue);
        loadListValue(key, client);
        splitPane.hide();
        if (isNew) {
            dataViewTabController.refreshThenSelect(key);
        }
        isNew = false;
        valueEdit.save();
        return true;
    }

    @Override
    public boolean save() throws IOException {
        if (valueList.isEmpty()) {
            Alert alert = new ProperAlert(Alert.AlertType.ERROR);
            alert.getButtonTypes().setAll(MyButtonType.OK);
            alert.setHeaderText(Language.getString("redis_save_empty_zset_error"));
            alert.showAndWait();
            return false;
        }
        if (!saveValue()) {
            return false;
        }
        if (!nameEditorPane.isSaved()) {
            if (!nameEditorPane.rename()) {
                return false;
            }
        }
        if (!ttlEditor.isSaved()) {
            return ttlEditor.setTtl();
        }
        return true;
    }

    @Override
    public boolean isSaved() {
        if (valueList.isEmpty()) {
            return false;
        }
        return nameEditorPane.isSavedBinding().get() && valueEdit.isSavedBinding().get() && ttlEditor.isSavedBinding().get();
    }
}
