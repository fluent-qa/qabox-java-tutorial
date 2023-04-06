package io.fluent.qabox.proxy.box;

import io.fluent.qabox.frontend.fun.FilterHandler;
import io.fluent.qabox.frontend.operation.Filter;
import io.fluent.qabox.util.di.IocUtil;
import org.aopalliance.intercept.MethodInvocation;
import io.fluent.qabox.constant.AnnotationConst;
import io.fluent.qabox.proxy.AnnotationProxy;


//TODO: remove spring dependency
public class FilterProxy<P> extends AnnotationProxy<Filter, P> {

    @Override
    protected Object invocation(MethodInvocation invocation) {
        if (AnnotationConst.VALUE.equals(invocation.getMethod().getName())) {
            String condition = this.rawAnnotation.value();
            if (!this.rawAnnotation.conditionHandler().isInterface()) {
                FilterHandler ch = IocUtil.getBean(this.rawAnnotation.conditionHandler());
                condition = ch.filter(condition, this.rawAnnotation.params());
            }
            return condition;
        }
        return this.invoke(invocation);
    }

}
