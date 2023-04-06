package io.fluent.qabox.module;


import io.fluent.qabox.config.Comment;

import java.util.List;

public interface BoxModule {

    @Comment("模块信息")
    ModuleInfo info();

    @Comment("初始化")
    default void run() {

    }

    @Comment("初始化菜单 → 仅执行一次，标识文件位置.erupt/.${moduleName}")
    default List<MetaMenu> initMenus() {
        return null;
    }

    @Comment("初始化方法 → 仅执行一次，标识文件位置.erupt/.${moduleName}")
    default void initFun() {

    }

}
