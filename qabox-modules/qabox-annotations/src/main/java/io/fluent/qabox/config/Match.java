package io.fluent.qabox.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Match {

    /**
     * 预注入变量值：
     * value : 当前注解变量
     * item :  父级注解变量
     *
     * @return 表达式
     */
    String value();
}
