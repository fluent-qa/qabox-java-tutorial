package io.fluent.qabox.proxy.box;

import io.fluent.qabox.Box;
import io.fluent.qabox.frontend.operation.Drill;
import io.fluent.qabox.frontend.operation.Filter;
import io.fluent.qabox.frontend.operation.RowOperation;
import io.fluent.qabox.processor.invoker.ExprResolverInvoke;
import io.fluent.qabox.proxy.AnnotationProxy;
import io.fluent.qabox.proxy.AnnotationProxyPool;

import lombok.SneakyThrows;
import org.aopalliance.intercept.MethodInvocation;


import java.util.ArrayList;
import java.util.List;


public class BoxProxy extends AnnotationProxy<Box, Void> {

    @Override
    @SneakyThrows
    protected Object invocation(MethodInvocation invocation) {
        switch (invocation.getMethod().getName()) {
            case "filter":
                Filter[] filters = this.rawAnnotation.filter();
                Filter[] proxyFilters = new Filter[filters.length];
                for (int i = 0; i < filters.length; i++) {
                    proxyFilters[i] = AnnotationProxyPool.getOrPut(filters[i], filter ->
                            new FilterProxy<Box>().newProxy(filter, this)
                    );
                }
                return proxyFilters;
            case "rowOperation":
                RowOperation[] rowOperations = this.rawAnnotation.rowOperation();
                List<RowOperation> proxyOperations = new ArrayList<>();
                for (RowOperation rowOperation : rowOperations) {
                    if (ExprResolverInvoke.getExpr(rowOperation.show())) {
                        proxyOperations.add(AnnotationProxyPool.getOrPut(rowOperation, it ->
                                new RowOperationProxy().newProxy(it, this)
                        ));
                    }
                }
                return proxyOperations.toArray(new RowOperation[0]);
            case "drills":
                Drill[] drills = this.rawAnnotation.drills();
                List<Drill> proxyDrills = new ArrayList<>();
                for (Drill drill : drills) {
                    if (ExprResolverInvoke.getExpr(drill.show())) {
                        proxyDrills.add(AnnotationProxyPool.getOrPut(drill, it ->
                                new DrillProxy().newProxy(it, this)
                        ));
                    }
                }
                return proxyDrills.toArray(new Drill[0]);
        }
        return this.invoke(invocation);
    }

}
