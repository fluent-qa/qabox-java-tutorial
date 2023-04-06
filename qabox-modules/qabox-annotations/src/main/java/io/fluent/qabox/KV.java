package io.fluent.qabox;

public @interface KV {
    String key();

    String value();

    String desc() default "";
}
