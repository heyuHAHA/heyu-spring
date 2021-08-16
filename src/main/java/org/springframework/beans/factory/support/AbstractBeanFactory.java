package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ConfigurableBeanFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();
    @Override
    public Object getBean(String name) throws BeansException {
        Object bean = getSingleton(name);
        if (bean != null) {
            return bean;
        }
        BeanDefinition beanDefinition = getBeanDefinition(name);
        return createBean(name,beanDefinition);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        //有则覆盖
        this.beanPostProcessorList.remove(beanPostProcessor);
        this.beanPostProcessorList.add(beanPostProcessor);
    }

    public  void destroySingletons() throws BeansException {
        Set<String> beanNames = disposableBeanMap.keySet();
        for (String beanName :beanNames) {
            DisposableBean disposableBean = disposableBeanMap.remove(beanName);
            try{
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("Destroy method on bean with name '" + beanName + "' threw an exception");
            }
        }
    }

    public List<BeanPostProcessor> getBeanPostProcessorList() {
        return beanPostProcessorList;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T) getBean(name);
    }

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException;

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;
}
