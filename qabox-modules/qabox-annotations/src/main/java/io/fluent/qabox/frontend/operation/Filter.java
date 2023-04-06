package io.fluent.qabox.frontend.operation;



import io.fluent.qabox.config.Comment;
import io.fluent.qabox.frontend.fun.FilterHandler;

import java.beans.Transient;

public @interface Filter {

    @Transient
    @Comment("数据过滤表达式")
    String value() default "";

    @Transient
    @Comment("可被conditionHandler获取")
    String[] params() default {};

    @Transient
    @Comment("动态处理过滤条件")
    Class<? extends FilterHandler> conditionHandler() default FilterHandler.class;
}
