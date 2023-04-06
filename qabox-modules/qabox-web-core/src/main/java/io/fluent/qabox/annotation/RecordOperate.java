package io.fluent.qabox.annotation;

import io.fluent.qabox.config.Comment;

import java.lang.annotation.*;
import java.lang.reflect.Method;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Comment("记录操作日志")
public @interface RecordOperate {

    @Comment("操作名称")
    String value();

    Class<? extends DynamicConfig> dynamicConfig() default DynamicConfig.class;

    interface DynamicConfig {
        String naming(String desc, String menuName, String eruptName, Method method);

        default boolean canRecord(String eruptName, Method method) {
            return true;
        }
    }

}
