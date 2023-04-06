package io.fluent.qabox.frontend.field;


import io.fluent.qabox.config.QueryExpression;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface EditTypeSearch {

    boolean value() default true;

    QueryExpression vagueMethod() default QueryExpression.EQ;
}
