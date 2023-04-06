package io.fluent.qabox.frontend.field.sub_edit;

import io.fluent.qabox.config.Comment;


public @interface Search {

    boolean value() default true;

    @Comment("高级查询")
    boolean vague() default false;

    @Comment("是否必填")
    boolean notNull() default false;

}
