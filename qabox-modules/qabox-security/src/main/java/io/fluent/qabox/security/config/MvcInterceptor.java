package io.fluent.qabox.security.config;

import io.fluent.qabox.config.constant.BoxRestPath;
import io.fluent.qabox.security.interceptor.BoxSecurityInterceptor;
import io.fluent.qabox.security.interceptor.HttpServletRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import javax.annotation.Resource;



@Configuration
public class MvcInterceptor implements WebMvcConfigurer {

    @Resource
    private BoxSecurityInterceptor boxSecurityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(boxSecurityInterceptor).addPathPatterns(BoxRestPath.ERUPT_API + "/**");
    }

    @Bean
    public FilterRegistrationBean<HttpServletRequestFilter> registerAuthFilter(BoxSecurityProp boxSecurityProp) {
        FilterRegistrationBean<HttpServletRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new HttpServletRequestFilter(boxSecurityProp));
        registration.addUrlPatterns(BoxRestPath.ERUPT_API + "/*");
        registration.setName(HttpServletRequestFilter.class.getSimpleName());
        registration.setOrder(1);
        return registration;
    }


}
