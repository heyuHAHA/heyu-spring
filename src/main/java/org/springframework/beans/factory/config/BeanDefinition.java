package org.springframework.beans.factory.config;

import org.springframework.beans.PropertyValues;

/**
 * 用于定义bean信息的类，包含bean的class类型、构造参数、属性值等信息，每个bean对应一个
 * BeanDefinition的实例。简化BeanDefinition仅包含bean的class类型
 */
public class BeanDefinition {
    private Class<?> beanClass;

    private PropertyValues propertyValues;

    public BeanDefinition(Class<?> beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.setBeanClass(beanClass);
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    /**
     * 提供修改
     * @param propertyValues
     */
    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }
}
