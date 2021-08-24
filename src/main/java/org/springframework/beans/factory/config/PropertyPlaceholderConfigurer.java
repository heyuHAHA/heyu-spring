package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.StringValueResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * 这个类是BeanFactoryPostProcessor，用来替换在配置文件中的${}值
 * AutowiredAnnotationBeanPostProcessor是针对注解{@Value}的，虽然也是替换${}值，但生命周期和这个类不一样
 */
public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {
    public static final String PLACEHOLDER_PREFIX = "${";
    public static final String PLACEHOLDER_SUFFIX = "}";

    private String location;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        //加载属性配置文件
        Properties properties = loadProperties();

        //属性值替换占位符
        processProperties(beanFactory,properties);

        //往容器中添加字符解析器， 供解析@value注解使用
        StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(properties);
        beanFactory.addEmbeddedValueResolver(valueResolver);

    }

    private void processProperties(ConfigurableListableBeanFactory beanFactory, Properties properties) throws BeansException {
        //获取BeanDefinitions
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();

        for (String beanName : beanDefinitionNames) {

            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            resolvePropertySetToBeanDefinition(beanDefinition,properties);
        }
    }

    private void resolvePropertySetToBeanDefinition(BeanDefinition beanDefinition, Properties properties) {

        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
            Object value = propertyValue.getValue();

            if (value instanceof String) {
                //
                value = resolvePlaceholder((String) value,properties);
                propertyValues.add(new PropertyValue(propertyValue.getName(),value));
            }
        }
    }

    private Properties loadProperties() {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        Properties properties = new Properties();
        try {
            properties.load(resource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }



    public void setLocation(String location) {
        this.location = location;
    }

    private String resolvePlaceholder(String value, Properties properties) {
        String strVal = value;
        StringBuffer buf = new StringBuffer(strVal);
        int startIndex = strVal.indexOf(PLACEHOLDER_PREFIX);
        int endIndex = strVal.indexOf(PLACEHOLDER_SUFFIX);
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            String key = strVal.substring(startIndex + 2, endIndex);
            String val = properties.getProperty(key);
            buf.replace(startIndex, endIndex + 1, val);
        }
        return buf.toString();

    }

    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {
        private final Properties properties;

        public PlaceholderResolvingStringValueResolver(Properties properties) {
            this.properties = properties;
        }

        @Override
        public String resolverStringValue(String strVal) {
            return PropertyPlaceholderConfigurer.this.resolvePlaceholder(strVal, properties);
        }
    }
}
