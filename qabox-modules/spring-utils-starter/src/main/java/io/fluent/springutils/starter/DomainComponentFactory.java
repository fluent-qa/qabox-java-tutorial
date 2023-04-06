package io.fluent.springutils.starter;

public class DomainComponentFactory {

    public static <T> T getOrCreate(Class<T> entityClz) {
        return ApplicationContextUtils.getBean(entityClz);
    }

}