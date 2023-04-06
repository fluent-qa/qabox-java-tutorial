package io.fluent.qabox.frontend.field.sub_edit;

import io.fluent.qabox.config.Comment;

public @interface CodeEditorType {

    @Comment("语言")
    String language();

    @Comment("编辑器高度")
    int height() default 300;

}
