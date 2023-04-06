package io.fluent.qabox.processor.invoker;


import io.fluent.qabox.expr.*;
import io.fluent.qabox.util.di.IocUtil;


//TODO: remove spring dep
public class ExprResolverInvoke {

    public static String getExpr(Expr expr) {
        String value = expr.value();
        if (!expr.exprHandler().isInterface()) {
            value = IocUtil.getBean(expr.exprHandler()).handler(value, expr.params());
        }
        return value;
    }

    public static Boolean getExpr(ExprBool expr) {
        boolean value = expr.value();
        if (!expr.exprHandler().isInterface()) {
            value = IocUtil.getBean(expr.exprHandler()).handler(value, expr.params());
        }
        return value;
    }

    public static int getExpr(ExprInt expr) {
        int value = expr.value();
        if (!expr.exprHandler().isInterface()) {
            value = IocUtil.getBean(expr.exprHandler()).handler(value, expr.params());
        }
        return value;
    }

    public static float getExpr(ExprFloat expr) {
        float value = expr.value();
        if (!expr.exprHandler().isInterface()) {
            value = IocUtil.getBean(expr.exprHandler()).handler(value, expr.params());
        }
        return value;
    }

    public static Class<?> getExpr(ExprClass expr) {
        Class<?> value = expr.value();
        if (!expr.exprHandler().isInterface()) {
            value = IocUtil.getBean(expr.exprHandler()).handler(value, expr.params());
        }
        return value;
    }


}
