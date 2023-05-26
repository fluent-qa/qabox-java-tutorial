package com.admin4j.plugin.loading;

import com.admin4j.plugin.LoadingStrategy;
import com.admin4j.plugin.configuration.ConfigurationReader;
import com.admin4j.plugin.configuration.reader.XmlConfigurationReader;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author andanyang
 * @since 2023/4/20 11:19
 */
public class ClassPathPluginLoadingStrategy implements LoadingStrategy {


    private static final Logger logger = LoggerFactory.getLogger(ClassPathPluginLoadingStrategy.class);

    @Getter
    protected ConfigurationReader configurationReader;

    public ClassPathPluginLoadingStrategy() {
        this(new XmlConfigurationReader());

    }

    public ClassPathPluginLoadingStrategy(ConfigurationReader configurationReader) {
        this.configurationReader = configurationReader;
    }

    @Override
    public boolean overridden() {
        return true;
    }

    @Override
    public int getPriority() {
        return LoadingStrategy.MAX_PRIORITY;
    }


    @Override
    public <T> Map<String, Class<T>> loadClass(Class<T> tClass) {

        //name -> Class
        Map<String, Class<T>> extensionClasses = new HashMap<>();
        String fileName = configurationReader.configurationLocation(tClass.getName());

        try {

            Enumeration<URL> urls = null;
            ClassLoader classLoader = findClassLoader();

            if (preferExtensionClassLoader()) {

                ClassLoader extensionLoaderClassLoader = ClassPathPluginLoadingStrategy.class.getClassLoader();
                if (ClassLoader.getSystemClassLoader() != extensionLoaderClassLoader) {
                    urls = extensionLoaderClassLoader.getResources(fileName);
                }
            }


            if (urls == null || !urls.hasMoreElements()) {
                if (classLoader != null) {
                    urls = classLoader.getResources(fileName);
                } else {
                    urls = ClassLoader.getSystemResources(fileName);
                }
            }

            if (urls != null) {
                while (urls.hasMoreElements()) {
                    java.net.URL resourceURL = urls.nextElement();
                    configurationReader.loadResource(extensionClasses, classLoader, resourceURL, tClass, excludedPackages());
                }
            }

        } catch (Exception e) {
            logger.error("Exception occurred when loading extension class (interface: " +
                    tClass.getName() + ", description file: " + fileName + ").", e);
        }

        return extensionClasses;
    }


    /**
     * 类加载器
     *
     * @return
     */
    protected ClassLoader findClassLoader() {

        ClassLoader cl = null;
        cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = LoadingStrategy.class.getClassLoader();
        }

        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
        }

        return cl;
    }
}
