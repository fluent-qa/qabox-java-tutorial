package io.fluentqa.redisbox.gui.util;


import io.fluentqa.redisbox.gui.controller.PrimaryController;

public class LogUtil {
    public static void log(String log) {
        PrimaryController.logStatusBar(log, "INFO");
    }
    public static void logw(String log) {
        PrimaryController.logStatusBar(log, "WARNING");
    }
}
