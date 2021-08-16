package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ConfigurableBeanFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.*;

/**
 * 定义了getBean的框架
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    private final Map<String, Object> factoryBeanCache = new HashMap<>();
    @Override
    public Object getBean(String beanName) throws BeansException {
       Object sharedInstance = getSingleton(beanName);
       if (sharedInstance != null) {
           return getObjectForBeanInstance(sharedInstance,beanName);
       }
       BeanDefinition beanDefinition = getBeanDefinition(beanName);
       sharedInstance = createBean(beanName,beanDefinition);
       return getObjectForBeanInstance(sharedInstance,beanName);
    }

    protected Object getObjectForBeanInstance(Object sharedInstance, String beanName) throws BeansException {
        Object bean = sharedInstance;
        if(sharedInstance instanceof FactoryBean) {
            FactoryBean factoryBean = (FactoryBean) sharedInstance;
            try {
                if (factoryBean.isSingleton()) {
                    bean = factoryBeanCache.get(beanName);
                    if (bean == null) {
                        bean = factoryBean.getObject();
                        factoryBeanCache.put(beanName, bean);
                    }
                } else {
                    bean = factoryBean.getObject();
                }

            } catch (Exception e) {
                throw new BeansException("FactoryBean threw exception on object[" + beanName + "] creation", e);
            }
        }
        return bean;
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
