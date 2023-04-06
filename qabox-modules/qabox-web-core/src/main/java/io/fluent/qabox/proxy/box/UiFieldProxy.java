package io.fluent.qabox.proxy.box;

import io.fluent.qabox.UIField;
import io.fluent.qabox.frontend.field.Edit;
import io.fluent.qabox.frontend.field.View;
import io.fluent.qabox.proxy.AnnotationProxy;
import io.fluent.qabox.proxy.AnnotationProxyPool;
import io.fluent.qabox.proxy.box.element.EditProxy;
import io.fluent.qabox.proxy.box.element.ViewProxy;
import lombok.SneakyThrows;
import org.aopalliance.intercept.MethodInvocation;


import java.util.ArrayList;
import java.util.List;


public class UiFieldProxy extends AnnotationProxy<UIField, Void> {

    private final AnnotationProxy<Edit, UIField> editAnnotationProxy = new EditProxy();

    @Override
    @SneakyThrows
    protected Object invocation(MethodInvocation invocation) {
        switch (invocation.getMethod().getName()) {
            case "views":
                View[] views = this.rawAnnotation.views();
                List<View> proxyViews = new ArrayList<>();
                for (View view : views) {
                    proxyViews.add(AnnotationProxyPool.getOrPut(view, annotation ->
                            new ViewProxy().newProxy(annotation, this)
                    ));
                }
                return proxyViews.toArray(new View[0]);
            case "edit":
                return AnnotationProxyPool.getOrPut(this.rawAnnotation.edit(), annotation ->
                        editAnnotationProxy.newProxy(annotation, this)
                );
        }
        return this.invoke(invocation);
    }

}
