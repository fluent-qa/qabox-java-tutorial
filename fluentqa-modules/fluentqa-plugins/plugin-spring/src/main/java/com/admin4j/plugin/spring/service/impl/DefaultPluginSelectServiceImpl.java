package com.admin4j.plugin.spring.service.impl;

import com.admin4j.api.anno.SPI;
import com.admin4j.plugin.spring.service.PluginSelectService;

/**
 * @author andanyang
 * @since 2023/4/24 9:38
 */
public class DefaultPluginSelectServiceImpl implements PluginSelectService {

    /**
     * 从配置中选择插件名称
     *
     * @param clazz
     * @return
     */
    @Override
    public String getPluginName(Class<?> clazz) {
        SPI annotation = clazz.getAnnotation(SPI.class);
        return annotation.value();
    }
}
