package com.admin4j.plugin.util;

/**
 * Helper Class for hold a value.
 *
 * @author andanyang
 * @since 2023/4/20 10:21
 */
public class Holder<T> {

    private volatile T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}
