package com.admin4j.plugin.spring.scan;

import com.admin4j.plugin.spring.factory.AdaptiveFactoryBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author andanyang
 * @since 2023/4/24 10:35
 */
@Slf4j
public class ClassPathMapperScanner extends ClassPathBeanDefinitionScanner {

    private final Class<? extends AdaptiveFactoryBean> mapperFactoryBeanClass = AdaptiveFactoryBean.class;
    private final boolean lazyInitialization = true;
    //private final String defaultScope = ConfigurableBeanFactory.SCOPE_SINGLETON;

    @Getter
    @Setter
    private Class<? extends Annotation> annotationClass;


    public ClassPathMapperScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters,
                                  Environment environment, @Nullable ResourceLoader resourceLoader) {
        super(registry, useDefaultFilters, environment, resourceLoader);
    }

    public void registerFilters() {

        // if specified, use the given annotation and / or marker interface
        if (this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
        }
        //addExcludeFilter(new AnnotationTypeFilter(this.annotationClass));
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {

        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {

            log.warn("No plugin mapper was found in '" + Arrays.toString(basePackages)
                    + "' package. Please check your Configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {

        AbstractBeanDefinition definition;
        BeanDefinitionRegistry registry = getRegistry();

        for (BeanDefinitionHolder holder : beanDefinitions) {

            definition = (AbstractBeanDefinition) holder.getBeanDefinition();
            //boolean scopedProxy = false;


            String beanClassName = definition.getBeanClassName();
            log.debug("Creating MapperFactoryBean with name '" + holder.getBeanName() + "' and '" + beanClassName
                    + "' mapperInterface");

            definition.setBeanClass(this.mapperFactoryBeanClass);

            String beanName = "PLUGIN#" + StringUtils.substringAfter(beanClassName, ".");
            AbstractBeanDefinition beanDefinition1 = BeanDefinitionBuilder.genericBeanDefinition(this.mapperFactoryBeanClass)
                    .addPropertyValue("mapperInterface", beanClassName)
                    //.addPropertyValue("pluginSelectService", new DefaultPluginSelectServiceImpl())
                    .addAutowiredProperty("pluginSelectService")
                    .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE)
                    .setRole(BeanDefinition.ROLE_INFRASTRUCTURE)
                    .setScope(definition.getScope())
                    .setLazyInit(lazyInitialization)

                    .getBeanDefinition();

            registry.registerBeanDefinition(beanName, beanDefinition1);
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        Map<String, Object> annotationAttributes = beanDefinition.getMetadata().getAnnotationAttributes(annotationClass.getName());
        Object adaptive = annotationAttributes.get("adaptive");
        if (adaptive == null || !((Boolean) adaptive)) {
            return false;
        }
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}
