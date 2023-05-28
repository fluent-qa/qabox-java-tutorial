package io.fluentqa.redisbox.gui.event;


import java.util.EventListener;

@FunctionalInterface
public interface EventHandler<T extends Event> extends EventListener {
    void handle(T var1);
}
