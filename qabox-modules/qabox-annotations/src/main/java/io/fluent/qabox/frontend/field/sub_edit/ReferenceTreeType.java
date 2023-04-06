package io.fluent.qabox.frontend.field.sub_edit;


import io.fluent.qabox.config.Comment;
import io.fluent.qabox.constant.AnnotationConst;
import io.fluent.qabox.expr.Expr;

import java.beans.Transient;

public @interface ReferenceTreeType {

  @Comment("存储列")
  String id() default AnnotationConst.ID;

  @Comment("展示列")
  String label() default AnnotationConst.LABEL;

  @Comment("父级节点标识列")
  String pid() default "";

  @Transient
  @Comment("标识pid为何特征才是根节点，需要与filter配合使用")
  Expr rootPid() default @Expr;

  @Comment("依赖字段")
  String dependField() default "";

  @Transient
  @Comment("与依赖字段值想关联的列名，dependField.value = this.dependColumn")
  String dependColumn() default AnnotationConst.ID;

  @Comment("展开层级数")
  int expandLevel() default 999;
}
