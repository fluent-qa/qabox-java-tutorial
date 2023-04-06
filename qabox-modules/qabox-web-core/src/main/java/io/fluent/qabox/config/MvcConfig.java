package io.fluent.qabox.config;


import com.google.gson.Gson;
import io.fluent.qabox.config.constant.BoxConst;
import io.fluent.qabox.config.constant.BoxRestPath;
import io.fluent.qabox.config.prop.BoxProp;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;



@RequiredArgsConstructor
@Configuration
@Component
public class MvcConfig implements WebMvcConfigurer {

    private final BoxProp appProp;

    private final Set<String> gsonMessageConverterPackage = Stream.of(BoxConst.BASE_PACKAGE, Gson.class.getPackage().getName()).collect(Collectors.toSet());

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        Optional.ofNullable(appProp.getGsonHttpMessageConvertersPackages()).ifPresent(it -> gsonMessageConverterPackage.addAll(Arrays.asList(it)));
        converters.add(0, new GsonHttpMessageConverter(GsonFactory.getGson()) {
            @Override
            protected boolean supports(Class<?> clazz) {
                for (String pack : gsonMessageConverterPackage) {
                    if (clazz.getName().startsWith(pack)) {
                        return super.supports(clazz);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = appProp.getUploadPath().endsWith("/") ? appProp.getUploadPath() : appProp.getUploadPath() + "/";
        ResourceHandlerRegistration resourceHandlerRegistration = registry.addResourceHandler(BoxRestPath.ERUPT_ATTACHMENT + "/**");
        if (uploadPath.startsWith("classpath:")) {
            resourceHandlerRegistration.addResourceLocations(uploadPath);
        } else {
            resourceHandlerRegistration.addResourceLocations("file:" + uploadPath);
        }
    }

}
