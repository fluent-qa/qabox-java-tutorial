package io.fluent.qabox.tpl.annotation;



import io.fluent.qabox.frontend.operation.Tpl;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface UITemplate {

    Tpl.Engine engine() default Tpl.Engine.FreeMarker;

}
