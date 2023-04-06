package io.fluent.qabox.proxy.box.element;

import io.fluent.qabox.frontend.field.Edit;
import io.fluent.qabox.frontend.field.Readonly;
import io.fluent.qabox.proxy.AnnotationProxy;
import io.fluent.qabox.util.di.IocUtil;
import org.aopalliance.intercept.MethodInvocation;


/**
 * readonly proxy
 */
public class ReadonlyProxy extends AnnotationProxy<Readonly, Edit> {

    @Override
    protected Object invocation(MethodInvocation invocation) {
        Readonly readonly = this.rawAnnotation;
        if (!readonly.exprHandler().isInterface()) {
            Readonly.ReadonlyHandler readonlyHandler = IocUtil.getBean(readonly.exprHandler());
            switch (invocation.getMethod().getName()) {
                case "add":
                    return readonlyHandler.add(readonly.add(), readonly.params());
                case "edit":
                    return readonlyHandler.edit(readonly.edit(), readonly.params());
            }
        }
        return this.invoke(invocation);
    }

}
