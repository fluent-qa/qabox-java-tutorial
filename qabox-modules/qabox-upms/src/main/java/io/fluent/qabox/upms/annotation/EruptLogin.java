package io.fluent.qabox.upms.annotation;



import io.fluent.qabox.config.Comment;

import java.lang.annotation.*;


@Comment("自定义登录逻辑，需在spring boot入口类中修饰")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface EruptLogin {

    Class<? extends LoginProxy> value();

}
