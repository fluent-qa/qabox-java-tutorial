package io.fluent.qabox.config;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface AutoFill {

    @Deprecated
    String value();

}
