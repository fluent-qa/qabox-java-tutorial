package com.admin4j.plugin.configuration.reader;

import com.admin4j.plugin.configuration.ConfigurationReader;
import com.admin4j.plugin.configuration.PluginConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.*;

/**
 * @author andanyang
 * @since 2023/4/25 9:11
 */

@Slf4j
public class XmlConfigurationReader implements ConfigurationReader {

    protected static Map<URL, List<PluginConfiguration>> cacheedParseXml = new HashMap<>(32);

    /**
     * 配置文件位置
     */
    @Override
    public String configurationLocation(String className) {
        return "plugin-config.xml";
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

            List<PluginConfiguration> configurations = parseXml(resourceURL);
            for (PluginConfiguration configuration : configurations) {

                List<PluginConfiguration.Plugin> plugins = configuration.getPlugins();
                for (PluginConfiguration.Plugin plugin : plugins) {

                    String interfaceName = plugin.getInterfaceName();
                    if (!Objects.equals(type.getName(), interfaceName)) {
                        continue;
                    }
                    String implementName = plugin.getImplementName();

                    if (StringUtils.isNotEmpty(implementName) && !isExcluded(implementName, excludedPackages)) {

                        Class<?> aClass = Class.forName(implementName, true, classLoader);

                        if (!type.isAssignableFrom(aClass)) {
                            throw new IllegalStateException("Error occurred when loading xml extension class (interface: " +
                                    type + ", class line: " + aClass.getName() + "), class "
                                    + aClass.getName() + " is not subtype of interface.");
                        }

                        extensionClasses.put(configuration.getName(), (Class<T>) aClass);
                    }
                }

            }
        } catch (Throwable t) {
            log.error("Exception occurred when loading extension class (interface: " +
                    type + ", class file: " + resourceURL + ") in " + resourceURL, t);
        }
    }

    protected PluginConfiguration.Plugin parsePlugin(Element plugin) {

        String anInterface = plugin.attributeValue("interface");
        String implementation = plugin.attributeValue("implementation");

        return new PluginConfiguration.Plugin(anInterface, implementation);
    }

    protected PluginConfiguration parsePlugins(Element plugins) {

        String name = plugins.attributeValue("name");
        String version = plugins.attributeValue("version");
        String description = plugins.attributeValue("description");

        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        pluginConfiguration.setName(name);
        pluginConfiguration.setVersion(version);
        pluginConfiguration.setDescription(description);

        return pluginConfiguration;
    }

    protected List<PluginConfiguration> parseXml(URL resourceURL) throws DocumentException {

        return cacheedParseXml.computeIfAbsent(resourceURL, (key) -> {

            try {
                return parseXmlFromUrl(key);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private List<PluginConfiguration> parseXmlFromUrl(URL resourceURL) throws DocumentException {
        List<PluginConfiguration> configurations = new ArrayList<PluginConfiguration>();
        SAXReader saxReader = new SAXReader();
        Document read = saxReader.read(resourceURL);
        // 获取根节点
        Element rootElement = read.getRootElement();
        Iterator<Element> elementIterator = rootElement.elementIterator();
        while (elementIterator.hasNext()) {
            //<plugins
            Element element = elementIterator.next();

            PluginConfiguration pluginConfiguration = parsePlugins(element);

            Iterator<Element> plugins = element.elementIterator("plugin");
            while (plugins.hasNext()) {
                PluginConfiguration.Plugin plugin = parsePlugin(plugins.next());

                if (StringUtils.isNotBlank(plugin.getInterfaceName()) && StringUtils.isNotBlank(plugin.getImplementName())) {
                    pluginConfiguration.getPlugins().add(plugin);
                }
            }

            configurations.add(pluginConfiguration);
        }

        return configurations;
    }

}
