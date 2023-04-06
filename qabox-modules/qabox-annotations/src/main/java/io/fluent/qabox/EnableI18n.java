package io.fluent.qabox;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface EnableI18n {

    boolean enable() default true;

}
