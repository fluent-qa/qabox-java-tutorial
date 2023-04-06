package io.fluent.qabox.frontend.field.sub_edit;

import io.fluent.qabox.config.Comment;
import io.fluent.qabox.frontend.fun.ChoiceFetchHandler;


import java.beans.Transient;


public @interface ChoiceType {

    @Comment("ui style")
    Type type() default Type.SELECT;

    @Transient
    @Comment("手动配置选择项")
    VL[] vl() default {};

    @Transient
    @Comment("可被fetchHandler接口获取到")
    String[] fetchHandlerParams() default {};

    @Transient
    @Comment("动态获取选择项")
    Class<? extends ChoiceFetchHandler>[] fetchHandler() default {};

    @Comment("开启后在编辑等操作时会重新获取下拉列表")
    boolean anewFetch() default false;

    enum Type {
        @Comment("下拉选择")
        SELECT,
        @Comment("单选框")
        RADIO,
    }
}