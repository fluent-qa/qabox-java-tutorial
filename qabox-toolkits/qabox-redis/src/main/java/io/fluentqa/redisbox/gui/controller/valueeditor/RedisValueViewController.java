package io.fluentqa.redisbox.gui.controller.valueeditor;

import io.fluentqa.redisbox.gui.controller.tabs.DataViewTabController;
import io.fluentqa.redisbox.service.RedisConnection;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

import java.io.IOException;

public interface RedisValueViewController {
    // 值是否被保存
    boolean isSaved();

    void initEdit(RedisConnection connection, int dbIndex, StringProperty key, DataViewTabController dataViewTabController);
    void initNew(RedisConnection connection, int dbIndex, StringProperty key, Stage stage, DataViewTabController dataViewTabController);
    void restore();

    boolean save() throws IOException;
}
