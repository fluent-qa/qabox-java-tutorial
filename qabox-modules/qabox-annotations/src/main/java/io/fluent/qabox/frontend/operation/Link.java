package io.fluent.qabox.frontend.operation;

import io.fluent.qabox.config.Comment;

import java.beans.Transient;


public @interface Link {

    @Comment("要关联的erupt类，注意：该类需要配置访问权限")
    Class<?> linkErupt();

    @Transient
    @Comment("被关联列，this.joinColumn = linkErupt.column")
    String column() default "id";

    @Transient
    @Comment("需要关联的列，this.joinColumn = linkErupt.column ")
    String joinColumn();
}
