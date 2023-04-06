package io.fluent.springutils.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SprintUtilsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ApplicationContextUtils.class)
    public ApplicationContextUtils applicationContextHelper() {
        return new ApplicationContextUtils();
    }
}