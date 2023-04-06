package io.fluent.qabox.frontend.field;


import io.fluent.qabox.config.Empty;
import io.fluent.qabox.config.JavaTypeEnum;

import java.lang.annotation.*;

/**
 * @author YuePeng
 * date 2019-05-24.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface EditTypeMapping {

    Class<? extends Annotation> mapping() default Empty.class;

    String desc() default "";

    JavaTypeEnum[] allowType() default {};

    boolean excelOperator() default true;

    String[] nameInfer() default {};
}
