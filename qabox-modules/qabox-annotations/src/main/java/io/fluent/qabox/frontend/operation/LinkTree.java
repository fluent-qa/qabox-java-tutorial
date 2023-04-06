package io.fluent.qabox.frontend.operation;

import io.fluent.qabox.config.Comment;

public @interface LinkTree {

    @Comment("树字段")
    String field();

    @Comment("表格的数据是否必须依赖树节点")
    boolean dependNode() default false;

}
