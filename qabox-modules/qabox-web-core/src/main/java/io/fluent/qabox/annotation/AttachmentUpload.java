package io.fluent.qabox.annotation;


import io.fluent.qabox.config.Comment;
import io.fluent.qabox.frontend.fun.AttachmentProxy;

import java.lang.annotation.*;

@Comment("自定义附件上传，需在spring boot入口类中修饰")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface AttachmentUpload {
    Class<? extends AttachmentProxy> value();
}
