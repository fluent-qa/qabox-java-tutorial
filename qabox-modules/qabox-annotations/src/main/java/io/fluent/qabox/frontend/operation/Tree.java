package io.fluent.qabox.frontend.operation;



import io.fluent.qabox.config.Comment;
import io.fluent.qabox.constant.AnnotationConst;
import io.fluent.qabox.expr.Expr;

import java.beans.Transient;


public @interface Tree {

    @Comment("树存储列")
    String id() default AnnotationConst.ID;

    @Comment("树展示列")
    String label() default AnnotationConst.LABEL;

    @Comment("父级节点标识列")
    String pid() default "";

    @Comment("展开层级数")
    int expandLevel() default 999;

    @Transient
    @Comment("标识pid为何特征才是根节点，需要与filter配合使用")
    Expr rootPid() default @Expr;

}
