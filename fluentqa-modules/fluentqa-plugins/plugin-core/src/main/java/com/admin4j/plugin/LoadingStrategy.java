package com.admin4j.plugin;


import com.admin4j.api.Prioritized;
import com.admin4j.plugin.configuration.ConfigurationReader;

import java.util.Map;

/**
 * 加载策略
 *
 * @author andanyang
 * @since 2023/4/20 10:00
 */
public interface LoadingStrategy extends Prioritized {

    /**
     * 是否启用
     *
     * @return
     */
    default boolean enable() {
        return true;
    }

    <T> Map<String, Class<T>> loadClass(Class<T> tClass);

    /**
     * 使用当前 ClassLoad
     *
     * @return
     */
    default boolean preferExtensionClassLoader() {
        return false;
    }

    default String[] excludedPackages() {
        return null;
    }

    /**
     * 是否允许覆盖
     *
     * @return
     */
    default boolean overridden() {
        return false;
    }

    ConfigurationReader getConfigurationReader();

}
