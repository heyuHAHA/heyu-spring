package org.springframework.context.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableAppliationContext;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.Map;

public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableAppliationContext {
    @Override
    public void refresh() throws BeansException {
        //创建BeanFactory，并加载BeanDefinition
        refreshBeanFactory();
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        //在bean实例化之前，执行BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        //BeanPostProcessor需要提前与其他bean实例化之前注册
        registerBeanPostProcessors(beanFactory);

        //提前实例化单例bean
        beanFactory.preInstantiateSingletons();
    }

    @Override
    public void registerShutdownHook() {
        Thread shutdownHook = new Thread() {
            public void run() {
                try {
                    doClose();
                } catch (BeansException e) {
                    e.printStackTrace();
                }
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    protected  void doClose() throws BeansException {
        getBeanFactory().destroySingletons();
    }

    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);

        }
    }

    protected  void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //获取是BeanFactoryPostProcessor类型的类
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    protected abstract void refreshBeanFactory() throws BeansException;

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name,requiredType);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);

    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }
}
