package io.fluent.qabox.frontend.field.sub_edit;

import io.fluent.qabox.config.Comment;

public @interface DateType {

    @Comment("时间类型")
    Type type() default Type.DATE;

    @Comment("挑选模式")
    PickerMode pickerMode() default PickerMode.ALL;

    enum Type {
        DATE,
        TIME,
        DATE_TIME,
        MONTH,
        WEEK,
        YEAR
    }

    enum PickerMode {
        @Comment("可选任意时间段")
        ALL,
        @Comment("仅可选择未来时间")
        FUTURE,
        @Comment("仅可选择历史时间")
        HISTORY
    }
}
