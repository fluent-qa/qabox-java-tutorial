package io.fluent.qabox.frontend.field.sub_edit;

import io.fluent.qabox.config.Comment;
import io.fluent.qabox.frontend.fun.TagsFetchHandler;


import java.beans.Transient;

public @interface TagsType {

    @Comment("多个标签存储时分割字符")
    String joinSeparator() default "|";

    @Comment("是否允许自定义标签")
    boolean allowExtension() default true;

    @Transient
    @Comment("可选标签列表")
    String[] tags() default {};

    @Transient
    @Comment("该配置可从fetchHandler中获取")
    String[] fetchHandlerParams() default {};

    @Transient
    @Comment("动态获取可选标签列表")
    Class<? extends TagsFetchHandler>[] fetchHandler() default {};
}
