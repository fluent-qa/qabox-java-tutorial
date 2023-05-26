package com.admin4j.plugin.spring.factory;

import com.admin4j.api.FilterPlugin;
import com.admin4j.api.PipelinePlugin;
import com.admin4j.api.PluginNameAWare;
import com.admin4j.api.anno.SPI;
import com.admin4j.plugin.ExtensionLoader;
import com.admin4j.plugin.spring.service.PluginSelectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author andanyang
 * @since 2023/4/25 15:01
 */
@Slf4j
@AllArgsConstructor
public class AdaptiveInvocationHandler<T> implements InvocationHandler {

    private Class<T> mapperInterface;
    private PluginSelectService pluginSelectService;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String pluginName = pluginSelectService.getPluginName(mapperInterface);
        if (StringUtils.isBlank(pluginName)) {
            SPI annotation = mapperInterface.getAnnotation(SPI.class);
            pluginName = annotation.value();
        }
        if (StringUtils.isBlank(pluginName)) {
            return null;
        }
        log.debug("Interface {} selected {}", mapperInterface, pluginName);


        if (PluginNameAWare.class.isAssignableFrom(mapperInterface)) {

            if (method.getName().equals("getPluginName")) {

                return pluginName;
            }
        }

        if (FilterPlugin.class.isAssignableFrom(mapperInterface) && method.getName().equals("start")) {

            List<T> allExtension = ExtensionLoader.getExtensionLoader(mapperInterface).getAllExtension();

            SimpleFilterChain<Object> filterChain = new SimpleFilterChain<>((List<FilterPlugin<Object>>) allExtension);

            filterChain.doFilter(args[0]);
            return null;
        }

        if (PipelinePlugin.class.isAssignableFrom(mapperInterface) && method.getName().equals("start")) {

            List<T> allExtension = ExtensionLoader.getExtensionLoader(mapperInterface).getAllExtension();

            for (T extension : allExtension) {
                PipelinePlugin plugin = (PipelinePlugin) extension;
                plugin.start(args[0]);
            }
            return null;
        }


        T extension = ExtensionLoader.getExtensionLoader(mapperInterface).getExtension(pluginName);
        Object invoke = method.invoke(extension, args);

        return invoke;
    }
}
