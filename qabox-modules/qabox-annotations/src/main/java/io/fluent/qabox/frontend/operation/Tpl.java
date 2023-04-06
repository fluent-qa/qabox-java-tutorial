package io.fluent.qabox.frontend.operation;

import lombok.Getter;
import io.fluent.qabox.config.Comment;

import java.beans.Transient;
import java.util.Map;

public @interface Tpl {

    @Transient
    @Comment("模板文件路径")
    String path();

    @Transient
    @Comment("定义的值可在tplHandler中获取到")
    String[] params() default {};

    @Transient
    @Comment("定义模板绑定数据")
    Class<? extends TplHandler> tplHandler() default TplHandler.class;

    @Transient
    @Comment("模板引擎")
    Engine engine() default Engine.FreeMarker;

    @Getter
    enum Engine {
        @Comment("原生H5, Native模式下不支持tplHandler")
        Native,
        @Comment("FreeMarker")
        FreeMarker,
        @Comment("Thymeleaf")
        Thymeleaf,
        @Comment("Velocity")
        Velocity,
        @Comment("Beetl")
        Beetl,
        @Comment(("JFinal Enjoy"))
        Enjoy
    }

    interface TplHandler {

        /**
         * 1.7.0 And above use the Function
         */
        void bindTplData(Map<String, Object> binding, String[] params);

    }

}
