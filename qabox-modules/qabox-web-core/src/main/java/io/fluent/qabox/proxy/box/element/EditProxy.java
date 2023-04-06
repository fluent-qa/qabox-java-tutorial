package io.fluent.qabox.proxy.box.element;

import io.fluent.qabox.UIField;
import io.fluent.qabox.frontend.field.Edit;
import io.fluent.qabox.frontend.field.EditType;
import io.fluent.qabox.frontend.operation.Filter;
import io.fluent.qabox.proxy.AnnotationProxy;
import io.fluent.qabox.proxy.AnnotationProxyPool;
import io.fluent.qabox.proxy.box.BoxProxyContext;
import io.fluent.qabox.proxy.box.FilterProxy;
import io.fluent.qabox.util.AnnotationUtil;
import io.fluent.qabox.util.BoxUtil;
import io.fluent.qabox.util.internal.TypeUtil;
import lombok.SneakyThrows;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ArrayUtils;

public class EditProxy extends AnnotationProxy<Edit, UIField> {

    @Override
    @SneakyThrows
    protected Object invocation(MethodInvocation invocation) {
        switch (invocation.getMethod().getName()) {
            case "type":
                if (EditType.AUTO == this.rawAnnotation.type()) {
                    String returnType = BoxProxyContext.get().getField().getType().getSimpleName();
                    if (boolean.class.getSimpleName().equalsIgnoreCase(returnType)) {
                        return EditType.BOOLEAN;
                    } else if (TypeUtil.isNumberType(returnType)) {
                        return EditType.NUMBER;
                    } else if (BoxUtil.isDateField(returnType)) {
                        return EditType.DATE;
                    } else if (ArrayUtils.contains(AnnotationUtil.getEditTypeMapping(EditType.TEXTAREA).nameInfer(), returnType)) {
                        return EditType.TEXTAREA; //属性名推断
                    } else {
                        return EditType.INPUT;
                    }
                }
                return this.rawAnnotation.type();
            case "filter":
                Filter[] filters = this.rawAnnotation.filter();
                Filter[] proxyFilters = new Filter[filters.length];
                for (int i = 0; i < filters.length; i++) {
                    proxyFilters[i] = AnnotationProxyPool.getOrPut(filters[i], filter -> new FilterProxy<Edit>().newProxy(filter, this));
                }
                return proxyFilters;
            case "readonly":
                return AnnotationProxyPool.getOrPut(this.rawAnnotation.readonly(), readonly -> new ReadonlyProxy().newProxy(readonly, this));
        }
        return this.invoke(invocation);
    }

}
