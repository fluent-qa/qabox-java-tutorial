package io.fluent.qabox.fun;

import io.fluent.qabox.config.Comment;
public interface PermissionHandler {

    @Comment("动态控制各功能使用权限")
    void handler(PermissionObject power);


}
