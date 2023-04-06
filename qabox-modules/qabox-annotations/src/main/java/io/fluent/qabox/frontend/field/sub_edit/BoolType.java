package io.fluent.qabox.frontend.field.sub_edit;


public @interface BoolType {
    String trueText() default "Y";

    String falseText() default "N";
}
