package io.fluent.qabox.proxy;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;



public class AnnotationProxyPool {

    /**
     * generic key raw annotation
     * generic value proxy annotation
     */
    private static final Map<Annotation, Annotation> annotationPool = new HashMap<>();

    public static <A extends Annotation> A getOrPut(A rawAnnotation, Function<A, A> function) {
        if (annotationPool.containsKey(rawAnnotation)) return (A) annotationPool.get(rawAnnotation);
        A proxyAnnotation = function.apply(rawAnnotation);
        annotationPool.put(rawAnnotation, proxyAnnotation);
        return proxyAnnotation;
    }

}
