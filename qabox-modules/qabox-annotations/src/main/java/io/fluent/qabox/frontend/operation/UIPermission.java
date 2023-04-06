package io.fluent.qabox.frontend.operation;

import io.fluent.qabox.config.Comment;
import io.fluent.qabox.fun.PermissionHandler;

import java.beans.Transient;


public @interface UIPermission {

    boolean add() default true;

    boolean edit() default true;

    boolean delete() default true;

    boolean query() default true;

    boolean viewDetails() default true;

    @Comment("导出")
    boolean export() default false;

    @Comment("导入")
    boolean importable() default false;

    @Transient
    @Comment("动态处理Power权限")
    Class<? extends PermissionHandler> permissionHandler() default PermissionHandler.class;
}
