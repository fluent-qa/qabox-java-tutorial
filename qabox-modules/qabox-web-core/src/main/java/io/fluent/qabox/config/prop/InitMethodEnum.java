package io.fluent.qabox.config.prop;


public enum InitMethodEnum {

    NONE,    //不执行初始化代码
    EVERY,   //每次启动都进行初始化
    FILE     //通过标识文件判断是否需要初始化

}
