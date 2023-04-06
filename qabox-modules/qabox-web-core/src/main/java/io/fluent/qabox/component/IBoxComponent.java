package io.fluent.qabox.component;

import java.lang.annotation.Annotation;


public interface IBoxComponent<A extends Annotation> {

    //验证参数值
    default void validate(A annotation, Object value) {

    }

}
