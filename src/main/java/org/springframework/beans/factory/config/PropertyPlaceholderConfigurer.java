package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

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
                String strVal = (String)value;
                StringBuffer buf = new StringBuffer(strVal);
                int startIndex = strVal.indexOf(PLACEHOLDER_PREFIX);
                int endIndex = strVal.indexOf(PLACEHOLDER_SUFFIX);
                if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                    String key = strVal.substring(startIndex+2,endIndex);
                    String val = properties.getProperty(key);
                    buf.replace(startIndex,endIndex+1,val);
                    propertyValues.add(new PropertyValue(propertyValue.getName(),buf.toString()));
                }
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
}
