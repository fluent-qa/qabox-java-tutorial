package io.fluent.qabox.annotation;



import io.fluent.qabox.config.Comment;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Comment("多数据源注解")
public @interface EnhancedDataSource {

    String value();
}
