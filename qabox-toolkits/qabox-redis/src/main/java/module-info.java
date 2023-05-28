module qabox.redis {
  requires javafx.controls;
  requires javafx.fxml;
  requires de.jensd.fx.glyphs.fontawesome;
  requires org.fxmisc.richtext;
  requires reactfx;
  requires com.google.gson;
  requires java.desktop;
  requires org.yaml.snakeyaml;
  requires org.controlsfx.controls;
  requires transitive javafx.graphics;
  requires undofx;
  opens io.fluentqa.redisbox.gui to javafx.graphics, javafx.fxml;
  opens io.fluentqa.redisbox.gui.controller to javafx.fxml, org.yaml.snakeyaml;
  opens io.fluentqa.redisbox.gui.controller.tabs to javafx.fxml;
  opens io.fluentqa.redisbox.gui.controller.valueeditor to javafx.fxml;
  opens io.fluentqa.redisbox.gui.component to javafx.fxml;

  exports io.fluentqa.redisbox;
  exports io.fluentqa.redisbox.service.client;
  exports io.fluentqa.redisbox.gui;
  exports io.fluentqa.redisbox.gui.component;
  exports io.fluentqa.redisbox.gui.controller;
  exports io.fluentqa.redisbox.gui.event;
  exports io.fluentqa.redisbox.gui.controller.valueeditor;
  exports io.fluentqa.redisbox.gui.controller.tabs;
  exports io.fluentqa.redisbox.service.protocol;
  exports io.fluentqa.redisbox.service;
  exports io.fluentqa.redisbox.gui.util;
}