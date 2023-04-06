package io.fluent.qabox.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD, ElementType.TYPE_PARAMETER, ElementType.PARAMETER})
@Documented
public @interface Comments {

    Comment[] value();

}