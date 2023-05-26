package com.admin4j.plugin.configuration.reader;

import com.admin4j.plugin.configuration.ConfigurationReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author andanyang
 * @since 2023/4/25 9:11
 */

@Slf4j
public class KeyValueConfigurationReader implements ConfigurationReader {


    /**
     * 配置文件位置
     *
     * @return 配置文件位置
     */
    @Override
    public String configurationLocation(String className) {
        return "META-INF/plugin/" + className;
    }

    /**
     * 加载配置
     */
    @Override
    public <T> void loadResource(Map<String, Class<T>> extensionClasses,
                                 ClassLoader classLoader,
                                 URL resourceURL, Class<T> type,
                                 String... excludedPackages) {

        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceURL.openStream(), StandardCharsets.UTF_8))) {
                String line;
                String clazz = null;
                while ((line = reader.readLine()) != null) {
                    final int ci = line.indexOf('#');
                    if (ci >= 0) {
                        line = line.substring(0, ci);
                    }
                    line = line.trim();
                    if (line.length() > 0) {
                        try {
                            String name = null;
                            int i = line.indexOf('=');
                            if (i > 0) {
                                name = line.substring(0, i).trim();
                                clazz = line.substring(i + 1).trim();
                            } else {
                                clazz = line;
                            }
                            if (StringUtils.isNotEmpty(clazz) && !isExcluded(clazz, excludedPackages)) {

                                Class<?> aClass = Class.forName(clazz, true, classLoader);

                                if (!type.isAssignableFrom(aClass)) {
                                    throw new IllegalStateException("Error occurred when loading extension class (interface: " +
                                            type + ", class line: " + aClass.getName() + "), class "
                                            + aClass.getName() + " is not subtype of interface.");
                                }

                                extensionClasses.put(name, (Class<T>) aClass);
                            }
                        } catch (Throwable t) {
                            IllegalStateException e = new IllegalStateException(
                                    "Failed to load extension class (interface: " + type + ", class line: " + line + ") in " + resourceURL +
                                            ", cause: " + t.getMessage(), t);
                            //exceptions.put(line, e);
                            throw e;
                        }
                    }
                }
            }
        } catch (Throwable t) {
            log.error("Exception occurred when loading extension class (interface: " +
                    type + ", class file: " + resourceURL + ") in " + resourceURL, t);
        }
    }
}
