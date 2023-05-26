package com.admin4j.plugin.spring.advanced;

import com.admin4j.api.ProviderPlugin;
import com.admin4j.plugin.ExtensionLoader;

import java.util.List;

/**
 * @author andanyang
 * @since 2023/4/25 16:57
 */
public class ProviderManager {

    private ProviderManager() {
    }

    public static <P extends ProviderPlugin<T>, T> P getProvider(Class<P> pClass, T t) {


        List<P> allExtension = ExtensionLoader.getExtensionLoader(pClass).getAllExtension();
        for (P i : allExtension) {
            if (i.supports(t)) {
                return i;
            }
        }

        return null;
    }


}
