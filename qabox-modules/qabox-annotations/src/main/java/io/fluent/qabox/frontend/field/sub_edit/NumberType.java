package io.fluent.qabox.frontend.field.sub_edit;


public @interface NumberType {
    long max() default Integer.MAX_VALUE;

    long min() default -Integer.MAX_VALUE;
}
