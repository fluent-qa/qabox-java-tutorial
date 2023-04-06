package io.fluent.qabox.frontend.field.sub_edit;

import io.fluent.qabox.config.Comment;
import io.fluent.qabox.constant.AnnotationConst;

public @interface CheckboxType {

    @Comment("存储列")
    String id() default AnnotationConst.ID;

    @Comment("展示列")
    String label() default AnnotationConst.LABEL;

}
