package com.admin4j.plugin.spring.service;

/**
 * @author andanyang
 * @since 2023/4/24 9:31
 */
public interface PluginSelectService {

    /**
     * 从配置中选择插件名称
     *
     * @param clazz
     * @return
     */
    String getPluginName(Class<?> clazz);
}
