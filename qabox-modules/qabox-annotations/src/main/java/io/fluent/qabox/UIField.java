package io.fluent.qabox;



import io.fluent.qabox.config.Comment;
import io.fluent.qabox.config.ToMap;
import io.fluent.qabox.frontend.field.Edit;
import io.fluent.qabox.frontend.field.View;

import java.beans.Transient;
import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface UIField {

    @Comment("表格列配置")
    View[] views() default {};

    @Comment("编辑组件配置")
    Edit edit() default @Edit(title = "");

    @Transient
    @Comment("显示顺序，默认按照字段排列顺序排序")
    int sort() default 1000;

    @ToMap(key = "key")
    @Comment("自定义扩展参数")
    KV[] params() default {};
}
