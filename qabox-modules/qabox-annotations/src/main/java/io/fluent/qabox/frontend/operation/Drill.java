package io.fluent.qabox.frontend.operation;



import io.fluent.qabox.config.AutoFill;
import io.fluent.qabox.config.Comment;
import io.fluent.qabox.expr.ExprBool;

import java.beans.Transient;


public @interface Drill {

    @Deprecated
    @AutoFill("T(Integer).toString(#item.title().hashCode())")
    String code() default "";

    String title();

    @Comment("图标请参考Font Awesome")
    String icon() default "fa fa-sitemap";

    @Comment("下钻目标配置")
    Link link();

    @Transient
    ExprBool show() default @ExprBool;

}
