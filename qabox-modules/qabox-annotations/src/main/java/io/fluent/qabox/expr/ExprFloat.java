package io.fluent.qabox.expr;

import java.beans.Transient;


public @interface ExprFloat {

    @Transient
    float value();

    @Transient
    String[] params() default {};

    @Transient
    Class<? extends ExprHandler> exprHandler() default ExprHandler .class;

    interface ExprHandler {

        /**
         * @param expr   表达式
         * @param params 注解参数
         * @return 程序处理后的表达式
         */
        float handler(float expr, String[] params);
    }
}
