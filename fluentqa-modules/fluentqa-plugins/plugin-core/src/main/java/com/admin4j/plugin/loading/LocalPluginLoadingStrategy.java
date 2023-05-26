package com.admin4j.plugin.loading;

import com.admin4j.plugin.PluginClassLoader;
import com.admin4j.plugin.PluginClassLoaderManager;
import com.admin4j.plugin.configuration.ConfigurationReader;
import com.admin4j.plugin.configuration.reader.XmlConfigurationReader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author andanyang
 * @since 2023/4/20 11:19
 */
public class LocalPluginLoadingStrategy extends ClassPathPluginLoadingStrategy {

    /**
     * 本地插件路径
     */
    private static final String PLUGIN_PATH = System.getProperty("PLUGIN_PATH", "./plugin");

    public LocalPluginLoadingStrategy() {
        this(new XmlConfigurationReader());

    }

    public LocalPluginLoadingStrategy(ConfigurationReader configurationReader) {
        this.configurationReader = configurationReader;
    }

    @Override
    public int getPriority() {
        return super.getPriority() - 100;
    }

    @Override
    public ClassLoader findClassLoader() {

        try {
            PluginClassLoader local = PluginClassLoaderManager.SHARE_INSTANCE.getPluginClassLoader("LOCAL");

            //遍历本地文件
            URL[] urls = Stream.of(PLUGIN_PATH).map(path -> new File(path))
                    .flatMap(file -> {
                        if (file.isDirectory()) {
                            try {
                                return Files.walk(Paths.get(file.getPath())).map(path -> path.toFile());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        } else {
                            return Stream.of(file);
                        }

                    }).filter(file -> file.getName().endsWith(".jar")).map(file -> {
                        try {
                            return new URL("jar:file:/" + file.getAbsolutePath() + "!/");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }).toArray(URL[]::new);


            local.addUrls(urls);
            return local;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
