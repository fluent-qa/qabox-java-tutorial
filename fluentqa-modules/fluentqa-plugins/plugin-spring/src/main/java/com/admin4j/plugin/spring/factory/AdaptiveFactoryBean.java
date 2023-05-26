package com.admin4j.plugin.spring.factory;

import com.admin4j.plugin.spring.service.PluginSelectService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author andanyang
 * @since 2023/4/21 14:20
 */
@Slf4j
@Data
//@RequiredArgsConstructor
public class AdaptiveFactoryBean<T> implements FactoryBean<T> {

    private Class<T> mapperInterface;
    private PluginSelectService pluginSelectService;


    @Override
    public T getObject() throws Exception {

        AdaptiveInvocationHandler<T> adaptiveInvocationHandler = new AdaptiveInvocationHandler<>(mapperInterface, pluginSelectService);
        return (T) Proxy.newProxyInstance(AdaptiveFactoryBean.class.getClassLoader(), new Class[]{mapperInterface}, adaptiveInvocationHandler);
    }


    @Override
    public Class<?> getObjectType() {
        return this.mapperInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
