package io.fluent.qabox.annotation;

import io.fluent.qabox.config.Comment;
import io.fluent.qabox.service.BoxAppBeanService;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({BoxAppBeanService.class})
public @interface BoxScan {

    @Comment("需要被扫描的包名")
    String[] value() default {};

}
