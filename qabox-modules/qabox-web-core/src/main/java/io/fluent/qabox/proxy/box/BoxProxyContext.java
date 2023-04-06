package io.fluent.qabox.proxy.box;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

@Getter
@Setter
public class BoxProxyContext {

    private static final ThreadLocal<BoxProxyContext> proxyContextThreadLocal = ThreadLocal.withInitial(BoxProxyContext::new);

    private Class<?> clazz;

    private Field field;

    public static void set(Class<?> clazz) {
        proxyContextThreadLocal.get().setClazz(clazz);
    }

    public static void set(Field field) {
        proxyContextThreadLocal.get().setField(field);
    }

    public static void remove() {
        proxyContextThreadLocal.remove();
    }

    public static BoxProxyContext get() {
        return proxyContextThreadLocal.get();
    }

}
