package io.fluent.qabox.frontend.fun;

import io.fluent.qabox.config.Comment;

public interface FilterHandler {

    String filter(@Comment("过滤表达式") String condition, @Comment("注解参数") String[] params);

}
