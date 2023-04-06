package io.fluent.qabox.frontend.field;


import io.fluent.qabox.config.Comment;
import io.fluent.qabox.config.QProperty;

import java.beans.Transient;

public @interface View {

    String title();

    String desc() default "";

    @Comment("列宽度（请指定单位如：%,px）")
    String width() default "";

    @Comment("修饰类型为实体类对象时必须指定列名")
    String column() default "";

    @QProperty(alias = "viewType")
    ViewType type() default ViewType.AUTO;

    boolean show() default true;

    @Comment("排序列")
    boolean sortable() default false;

    @Transient
    @Comment("导出列")
    boolean export() default true;

    @Comment("样式类名")
    String className() default "";

    @Comment("格式化表格列值，前端使用eval方法解析，支持变量：" +
            "1、item    （表格整行数据）" +
            "2、item.xxx（数据行中某一列的值）" +
            "3、value   （当前列值）")
    String template() default "";

}
