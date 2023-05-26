package com.admin4j.api;

/**
 * @author andanyang
 * @since 2023/4/25 14:38
 */
public interface PluginNameAWare {


    /**
     * @return 当前插件名称
     */
    default String getPluginName() {
        return null;
    }
}
