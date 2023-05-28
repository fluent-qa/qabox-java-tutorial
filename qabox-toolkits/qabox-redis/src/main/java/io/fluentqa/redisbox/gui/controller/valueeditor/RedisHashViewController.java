package io.fluentqa.redisbox.gui.controller.valueeditor;

import io.fluentqa.redisbox.gui.AdaptiveSplitPane;
import io.fluentqa.redisbox.gui.component.NameEditorPane;
import io.fluentqa.redisbox.gui.component.TTLEditorPane;
import io.fluentqa.redisbox.gui.component.ValueEditorPane;
import io.fluentqa.redisbox.gui.controller.tabs.DataViewTabController;
import javafx.beans.InvalidationListener;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.stage.Stage;
import io.fluentqa.redisbox.gui.ProperAlert;
import io.fluentqa.redisbox.gui.buttontype.MyButtonType;
import io.fluentqa.redisbox.service.RedisConnection;
import io.fluentqa.redisbox.base.StringUtils;
import io.fluentqa.redisbox.service.client.AuthError;
import io.fluentqa.redisbox.service.client.Pair;
import io.fluentqa.redisbox.service.client.debug.Debugger;
import io.fluentqa.redisbox.service.client.RedisClient;
import io.fluentqa.redisbox.service.client.debug.result.DebugResult;
import io.fluentqa.redisbox.service.client.debug.result.EndResult;
import io.fluentqa.redisbox.service.client.debug.result.ErrorResult;
import io.fluentqa.redisbox.service.client.debug.result.PauseResult;
import io.fluentqa.redisbox.gui.App;
import io.fluentqa.redisbox.gui.LuaCodeField;
import io.fluentqa.redisbox.gui.cell.ConnectionListCell;
import io.fluentqa.redisbox.service.protocol.*;
import io.fluentqa.redisbox.base.FileUtil;
import io.fluentqa.redisbox.base.Language;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class RedisHashViewController implements RedisValueViewController {
    public NameEditorPane nameEditorPane;

    public TableView<Map<String, String>> valueTableView;

    public TableColumn<Map<String, String>, String> keyColumn;
    public TableColumn<Map<String, String>, String> valueColumn;

    public TextField editFieldTextField;
    public AdaptiveSplitPane splitPane;
    public ValueEditorPane valueEdit;
    public MenuItem removeMenuItem;
    public TTLEditorPane ttlEditor;
    public Button saveButton;

    private RedisClient client;

    ObservableList<Map<String, String>> dataList = FXCollections.observableList(new ArrayList<>());
    private DataViewTabController dataViewTabController;

    private void init(StringProperty key) {
        splitPane.hide();
        valueTableView.setItems(dataList);
        valueTableView.getSelectionModel().getSelectedIndices().addListener((InvalidationListener) observable -> {
            Map<String, String> keyValueMap = valueTableView.getSelectionModel().getSelectedItem();
            if (keyValueMap != null) {
                editFieldTextField.setText(keyValueMap.get("key"));
                valueEdit.setText(keyValueMap.get("value"));
                splitPane.show();
            }
        });
        valueTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        valueColumn.setCellFactory(TableValueFactory.factory);
        ttlEditor.init(client, key);
        nameEditorPane.init(client, key, dataViewTabController);
    }


    @Override
    public void initEdit(RedisConnection connection, int dbIndex, StringProperty key, DataViewTabController dataViewTabController) {
        this.dataViewTabController = dataViewTabController;
        this.client = connection.getClient();
        init(key);
        retrieveData();
    }

    private boolean isNew = false;

    @Override
    public void initNew(RedisConnection connection, int dbIndex, StringProperty key, Stage stage, DataViewTabController dataViewTabController) {
        this.dataViewTabController = dataViewTabController;
        this.client = connection.getClient();
        init(key);
        isNew = true;
    }

    @Override
    public void restore() {
        nameEditorPane.restore();
        ttlEditor.restore();
        valueEdit.restore();
    }

    @Override
    public boolean save() throws IOException {
        if (dataList.isEmpty()) {
            Alert alert = new ProperAlert(Alert.AlertType.ERROR);
            alert.getButtonTypes().setAll(MyButtonType.OK);
            alert.setHeaderText(Language.getString("redis_save_empty_hash_error"));
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

    private void retrieveData() {
        String key = nameEditorPane.getCurrentKey();
        try {
            dataList.clear();
            RespArray<RespString> dataArray = client.hgetall(key);
            for (int i = 0; i < dataArray.size(); i += 2) {
                String field = dataArray.get(i).getValue();
                String value = dataArray.get(i + 1).getValue();
                Map<String, String> kv = Map.of("key", field, "value", value);
                dataList.add(kv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addKey() {
        valueTableView.getSelectionModel().clearSelection();
        editFieldTextField.clear();
        valueEdit.clear();
        splitPane.show();
    }

    public void cancel() {
        splitPane.hide();
        valueTableView.getSelectionModel().clearSelection();
    }

    public boolean saveValue() throws IOException {
        Map<String, String> selectedItem = valueTableView.getSelectionModel().getSelectedItem();
        String key = nameEditorPane.getCurrentKey();
        if (StringUtils.isEmpty(key)) {
            Alert alert = new ProperAlert(Alert.AlertType.ERROR);
            alert.getButtonTypes().setAll(MyButtonType.OK);
            alert.setHeaderText(Language.getString("redis_alert_key_empty"));
            alert.showAndWait();
            return false;
        }
        if (selectedItem != null) {
            client.hdel(key, selectedItem.get("key"));
        }
        client.hset(key, editFieldTextField.getText(), valueEdit.getText());
        valueEdit.save();
        splitPane.hide();
        retrieveData();

        if (isNew) {
            dataViewTabController.refreshThenSelect(key);
        }
        isNew = false;
        return true;
    }


    public void remove() {
        ObservableList<Map<String, String>> selectedItems = valueTableView.getSelectionModel().getSelectedItems();
        Alert alert = new ProperAlert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(MyButtonType.YES, MyButtonType.NO);
        String name = valueTableView.getSelectionModel().getSelectedItem().get("key");
        if (selectedItems.size() == 1) {
            alert.setHeaderText(String.format(Language.getString("redis_delete_name"), name));
        } else {
            alert.setHeaderText(String.format(Language.getString("redis_delete_count"), selectedItems.size()));
        }
        alert.showAndWait().ifPresent(type -> {
            String key = nameEditorPane.getCurrentKey();
            if (type == MyButtonType.YES) {
                for (Map<String, String> selectedItem : selectedItems) {
                    try {
                        client.hdel(key, selectedItem.get("key"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                retrieveData();
            }
        });
    }

    @Override
    public boolean isSaved() {
        if (dataList.isEmpty()) {
            return false;
        }
        return nameEditorPane.isSavedBinding().get() && valueEdit.isSavedBinding().get() && ttlEditor.isSavedBinding().get();
    }
}
