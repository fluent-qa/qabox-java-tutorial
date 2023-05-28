package io.fluentqa.redisbox.gui.controller.valueeditor;

import io.fluentqa.redisbox.gui.AdaptiveSplitPane;
import io.fluentqa.redisbox.gui.component.NameEditorPane;
import io.fluentqa.redisbox.gui.component.TTLEditorPane;
import io.fluentqa.redisbox.gui.component.ValueEditorPane;
import io.fluentqa.redisbox.gui.controller.tabs.DataViewTabController;
import io.fluentqa.redisbox.gui.util.GuiUtils;
import javafx.beans.InvalidationListener;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.SelectionMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RedisListViewController implements RedisValueViewController {
    public NameEditorPane nameEditorPane;

    public TableView<Map<String, String>> valueTableView;
    private final ObservableList<Map<String, String>> valueList = FXCollections.observableList(new ArrayList<>());

    public TableColumn<Map<String, String>, String> indexColumn;
    public TableColumn<Map<String, String>, String> valueColumn;

    public MenuItem removeMenuItem;
    public AdaptiveSplitPane splitPane;
    public TTLEditorPane ttlEditor;
    public ValueEditorPane valueEdit;
    private RedisClient client;
    private Integer selectedIndex = null;
    private DataViewTabController dataViewTabController;

    private void init(StringProperty key) {
        valueTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        splitPane.hide();
        valueTableView.setItems(valueList);
        valueTableView.getStylesheets().add(GuiUtils.getResourcePath("css/list-editor.css"));
        valueTableView.getSelectionModel().getSelectedIndices().addListener((InvalidationListener) listener -> {
            boolean empty = valueTableView.getSelectionModel().getSelectedIndices().isEmpty();
            removeMenuItem.setDisable(empty);

            selectedIndex = valueTableView.getSelectionModel().getSelectedIndex();
            if (valueTableView.getSelectionModel().getSelectedIndices().size() == 0 || valueTableView.getSelectionModel().getSelectedIndices().size() > 1) {
                return;
            }
            splitPane.show();
            if (valueTableView.getSelectionModel().getSelectedItem() != null) {
                valueEdit.setText(valueTableView.getSelectionModel().getSelectedItem().get("value"));
            }
        });
        valueEdit.textProperty().addListener(observable -> {
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
        RespArray<RespString> values = client.lrange(key);
        valueList.clear();
        if (values == null) {
            return;
        }
        int index = 1;
        for (RespString value : values) {
            valueList.add(Map.of("index", String.valueOf(index++), "value", String.valueOf(value.getValue())));
        }
    }

    private boolean isNew = false;

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
        List<Integer> selectedIndices = new ArrayList<>(valueTableView.getSelectionModel().getSelectedIndices());
        Alert alert = new ProperAlert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(MyButtonType.YES, MyButtonType.NO);
        alert.setTitle(Language.getString("redis_delete_confirmation"));
        String name = valueTableView.getSelectionModel().getSelectedItem().get("value");
        // 保证从后面删除 为了保证不影响前面元素的位置
        selectedIndices.sort((a, b) -> Integer.compare(b, a));

        if (selectedIndices.size() == 1) {
            alert.setHeaderText(String.format(Language.getString("redis_delete_name"), name));
        } else {
            alert.setHeaderText(String.format(Language.getString("redis_delete_count"), selectedIndices.size()));
        }
        alert.getDialogPane().getStylesheets().add(GuiUtils.getResourcePath("css/button.css"));
        alert.showAndWait().ifPresent(type -> {
                    String key = nameEditorPane.getCurrentKey();
                    if (type == MyButtonType.YES) {
                        try {
                            if (selectedIndices.size() == 1) {
                                client.ldelete(key, selectedIndex);
                            } else {
                                for (int index : selectedIndices) {
                                    client.ldelete(key, index);
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
        var selectionModel = valueTableView.getSelectionModel();
        if (!selectionModel.getSelectedIndices().isEmpty()) {
            int selectedIndex = selectionModel.getSelectedIndex();
        }
    }


    public void cancel() {
        splitPane.hide();
        valueTableView.getSelectionModel().clearSelection();
        selectedIndex = null;
    }

    public void newValue() {
        splitPane.show();
        valueEdit.clear();
        valueTableView.getSelectionModel().clearSelection();
        selectedIndex = null;
        top = false;
    }

    boolean top = false;

    public void newValueTop() {
        splitPane.show();
        valueEdit.clear();
        valueTableView.getSelectionModel().clearSelection();
        selectedIndex = null;
        top = true;
    }

    public boolean saveValue() throws IOException {
        String key = nameEditorPane.getCurrentKey();
        if (StringUtils.isEmpty(key)) {
            Alert alert = new ProperAlert(Alert.AlertType.ERROR);
            alert.getButtonTypes().setAll(MyButtonType.OK);
            alert.setHeaderText(Language.getString("redis_alert_key_empty"));
            alert.showAndWait();
            return false;
        }

        if (selectedIndex != null) {
            client.lset(key, selectedIndex, valueEdit.getText());
            loadListValue(key, client);
            valueTableView.getSelectionModel().select(selectedIndex);
        } else if (top) {
            client.lpush(key, valueEdit.getText());
            loadListValue(key, client);
            valueTableView.getSelectionModel().selectFirst();
        } else {
            client.rpush(key, valueEdit.getText());
            loadListValue(key, client);
            valueTableView.getSelectionModel().selectLast();
        }
        valueEdit.save();
        splitPane.hide();

        if (isNew) {
            dataViewTabController.refreshThenSelect(key);
        }
        isNew = false;
        return true;
    }

    @Override
    public boolean save() throws IOException {
        if (valueList.isEmpty()) {
            Alert alert = new ProperAlert(Alert.AlertType.ERROR);
            alert.getButtonTypes().setAll(MyButtonType.OK);
            alert.setHeaderText(Language.getString("redis_save_empty_list_error"));
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
