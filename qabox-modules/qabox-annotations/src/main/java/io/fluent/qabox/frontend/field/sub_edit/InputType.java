package io.fluent.qabox.frontend.field.sub_edit;

import io.fluent.qabox.config.Comment;

import java.beans.Transient;


public @interface InputType {

    @Comment("最大输入长度")
    int length() default 255;

    String type() default "text";

    @Comment("是否整行显示")
    boolean fullSpan() default false;

    @Transient
    @Comment("对提交内容进行正则校验")
    String regex() default "";

    VL[] prefix() default {};

    VL[] suffix() default {};
}
