package com.admin4j.plugin.configuration;

import java.util.Map;

/**
 * @author andanyang
 * @since 2023/4/24 15:11
 */
public interface ConfigurationReader {

    /**
     * 配置文件位置
     *
     * @return
     */
    String configurationLocation(String className);

    /**
     * 加载配置
     */
    <T> void loadResource(Map<String, Class<T>> extensionClasses,
                          ClassLoader classLoader,
                          java.net.URL resourceURL,
                          Class<T> type,
                          String... excludedPackages);


    /**
     * 是否排除
     *
     * @return 是否排除该类
     */
    default boolean isExcluded(String className, String... excludedPackages) {
        if (excludedPackages != null) {
            for (String excludePackage : excludedPackages) {
                if (className.startsWith(excludePackage + ".")) {
                    return true;
                }
            }
        }
        return false;
    }
}
