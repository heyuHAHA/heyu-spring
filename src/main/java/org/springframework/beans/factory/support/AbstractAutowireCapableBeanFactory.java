package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{

    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        return doCreateBean(beanName,beanDefinition);
    }

    private Object doCreateBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        Object bean = null;
        try {
            bean = createBeanInstance(beanDefinition);
            //为Bean填充属性
            applyPropertyValues(beanName,bean,beanDefinition);
        } catch (Exception e) {
            throw new BeansException();
        }
        addSingleton(beanName,bean);
        return bean;
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition) throws BeansException {
        return getInstantiationStrategy().instantiate(beanDefinition);
    }

    /**
     * 为Bean填充属性
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        PropertyValue pv = null;
        try {
            PropertyValue[] propertyValues = beanDefinition.getPropertyValues().getPropertyValues();
            for (PropertyValue propertyValue : propertyValues) {
                pv = propertyValue;
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                //TODO
                // BeanUtil.apply(bean, name, value);
            }
        } catch (Exception e) {

            //throw new BeansApplyPropertiesExceptions("apply " + pv.getName() + " : " + pv.getValue()  + " to " + beanName +" occurs a error" );
        }
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }


}
