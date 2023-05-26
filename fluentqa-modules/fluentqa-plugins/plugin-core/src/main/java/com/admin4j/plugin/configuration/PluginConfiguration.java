package com.admin4j.plugin.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andanyang
 * @since 2023/4/24 16:15
 */
@Data
public class PluginConfiguration {

    /**
     * 插件名称
     */
    private String name;
    private String version;
    private String description;

    private List<Plugin> plugins = new ArrayList<Plugin>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static public class Plugin {
        private String interfaceName;
        private String implementName;
    }
}
