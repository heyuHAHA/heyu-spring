package org.springframework.context.support;

import cn.hutool.core.lang.Assert;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableAppliationContext;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;

public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableAppliationContext {

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    private ApplicationEventMulticaster applicationEventMulticaster;

    @Override
    public void refresh() throws BeansException {
        //创建BeanFactory，并加载BeanDefinition
        refreshBeanFactory();
        //
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        //注册一个ApplicationContextAware的BeanPostProcessor
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
        //在bean实例化之前，执行BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        //BeanPostProcessor需要提前与其他bean实例化之前注册
        registerBeanPostProcessors(beanFactory);
        //初始化事件发布者
        initApplicationEventMulticaster();

        //注册事件监听器
        registerListeners();

        //提前实例化单例bean
        beanFactory.preInstantiateSingletons();

        //发布容器刷新完成事件
        finishRefresh();
    }

    protected void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.addSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME,applicationEventMulticaster);
    }

    /**
     * 注册事件监听器
     */
    protected void registerListeners() throws BeansException {
        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener applicationListener :applicationListeners) {
            applicationEventMulticaster.addApplicationListener(applicationListener);
        }
    }

    protected void finishRefresh() throws BeansException {
        publishEvent(new ContextRefreshedEvent(this));
    }

    public void publishEvent(ApplicationEvent event) throws BeansException {
        applicationEventMulticaster.multicastEvent(event);
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

    protected void doClose() throws BeansException {
        //发布容器关闭事件
        publishEvent(new ContextClosedEvent(this));
        getBeanFactory().destroySingletons();
    }

    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        System.out.println(beanPostProcessorMap.values());
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            System.out.println(beanPostProcessor);
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
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(requiredType);
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
