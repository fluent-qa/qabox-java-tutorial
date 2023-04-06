package io.fluent.qabox.frontend.field.sub_edit;

import io.fluent.qabox.config.Comment;

public @interface HtmlEditorType {

    @Comment("富文本编辑器类型")
    Type value();

    enum Type {
        CKEDITOR, UEDITOR
    }
}
