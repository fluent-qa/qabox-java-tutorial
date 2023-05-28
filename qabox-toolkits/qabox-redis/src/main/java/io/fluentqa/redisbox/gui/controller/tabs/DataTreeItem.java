package io.fluentqa.redisbox.gui.controller.tabs;


import io.fluentqa.redisbox.service.client.RedisType;

public class DataTreeItem {
    String text;
    RedisType type;

    public DataTreeItem(String text, RedisType type) {
        this.text = text;
        this.type = type;
    }
}
