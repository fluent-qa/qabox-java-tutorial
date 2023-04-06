package io.fluent.qabox.proxy;

import lombok.SneakyThrows;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;

//TODO : Resolve Spring Dep
public abstract class AnnotationProxy<A, PA> {

    // 原始注解
    public A rawAnnotation;

    // 代理后新注解
    public A proxyAnnotation;

    // 向上引用
    protected AnnotationProxy<PA, ?> parent;

    protected abstract Object invocation(MethodInvocation invocation);

    //创建注解注解代理类
    public A newProxy(A annotation, AnnotationProxy<PA, ?> parent) {
        this.parent = parent;
        this.rawAnnotation = annotation;
        ProxyFactory proxyFactory = new ProxyFactory(annotation);
        MethodInterceptor interceptor = this::invocation;
        proxyFactory.addAdvice(interceptor);
        this.proxyAnnotation = (A) proxyFactory.getProxy(this.getClass().getClassLoader());
        return this.proxyAnnotation;
    }

    // annotation method invoke
    @SneakyThrows
    public Object invoke(MethodInvocation invocation) {
        return invocation.getMethod().invoke(invocation.getThis());
    }

}
