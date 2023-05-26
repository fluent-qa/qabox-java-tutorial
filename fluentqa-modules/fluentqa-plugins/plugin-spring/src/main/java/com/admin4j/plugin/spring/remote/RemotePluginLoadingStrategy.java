package com.admin4j.plugin.spring.remote;

import com.admin4j.plugin.ExtensionLoader;
import com.admin4j.plugin.LoadingStrategy;
import com.admin4j.plugin.PluginClassLoader;
import com.admin4j.plugin.PluginClassLoaderManager;
import com.admin4j.plugin.configuration.ConfigurationReader;
import com.admin4j.plugin.configuration.reader.XmlConfigurationReader;
import com.admin4j.plugin.loading.LocalPluginLoadingStrategy;

import java.io.IOException;
import java.util.List;

/**
 * jar 放在 OSS 上
 *
 * @author andanyang
 * @since 2023/4/24 16:22
 */
public class RemotePluginLoadingStrategy extends LocalPluginLoadingStrategy {


    private final RemoteJarService remoteJarService;

    public RemotePluginLoadingStrategy() {
        this(new XmlConfigurationReader(), null);
    }

    public RemotePluginLoadingStrategy(ConfigurationReader configurationReader, RemoteJarService remoteJarService) {
        this.configurationReader = configurationReader;
        this.remoteJarService = remoteJarService;

        //加入扩展
        LoadingStrategy[] loadingStrategies = ExtensionLoader.getLoadingStrategies();
        LoadingStrategy[] LoadingStrategyNew = new LoadingStrategy[loadingStrategies.length + 1];

        System.arraycopy(loadingStrategies, 0, LoadingStrategyNew, 0, loadingStrategies.length);

        LoadingStrategyNew[loadingStrategies.length] = this;

        ExtensionLoader.setLoadingStrategies(LoadingStrategyNew);
    }

    @Override
    public boolean enable() {

        return true;
    }

    @Override
    public int getPriority() {
        return super.getPriority() - 10;
    }

    @Override
    public ClassLoader findClassLoader() {

        try {
            PluginClassLoader remote = PluginClassLoaderManager.SHARE_INSTANCE.getPluginClassLoader("REMOTE");

            //RemoteJarService bean = SpringUtils.getBean(RemoteJarService.class);
            //this.remoteJarService = bean;

            //遍历远程文件
            List<String> allJarUrl = remoteJarService.getAllJarUrl();
            for (String url : allJarUrl) {
                remote.addHttpUrl(url);
            }

            return remote;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
