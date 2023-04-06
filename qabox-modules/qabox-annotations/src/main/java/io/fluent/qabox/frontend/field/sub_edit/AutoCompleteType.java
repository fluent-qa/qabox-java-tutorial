package io.fluent.qabox.frontend.field.sub_edit;

import io.fluent.qabox.config.Comment;
import io.fluent.qabox.frontend.fun.AutoCompleteHandler;


import java.beans.Transient;

public @interface AutoCompleteType {

    @Transient
    @Comment("动态生成自动完成列表")
    Class<? extends AutoCompleteHandler> handler();

    @Transient
    @Comment("可在handler中获取到")
    String[] param() default {};

    @Comment("触发字符最小长度")
    int triggerLength() default 1;
}
