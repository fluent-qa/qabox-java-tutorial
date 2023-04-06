package io.fluent.qabox.frontend.field.sub_edit;

import io.fluent.qabox.config.Comment;

public @interface ShowBy {

    @Comment("依赖字段名")
    String dependField();

    @Comment("显示条件表达式，支持变量：value 该值表示依赖字段的值")
    String expr();

}
