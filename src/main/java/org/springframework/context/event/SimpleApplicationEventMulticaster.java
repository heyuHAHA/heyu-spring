package org.springframework.context.event;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster{

    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
        setBeanFactory(beanFactory);
    }

    @Override
    public void multicastEvent(ApplicationEvent event) throws BeansException {
        for (ApplicationListener<ApplicationEvent> applicationListener : applicationListeners) {
            if (supportsEvent(applicationListener,event)) {
                applicationListener.onApplicationEvent(event);
            }
        }
    }

    protected boolean supportsEvent(ApplicationListener<ApplicationEvent> applicationListener, ApplicationEvent event) throws BeansException {
        Type type = applicationListener.getClass().getGenericInterfaces()[0];
        Type actualTypeArgument = ((ParameterizedType)type).getActualTypeArguments()[0];
        String typeName = actualTypeArgument.getTypeName();
        Class<?> clazz = null;
        try {
            clazz = Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new BeansException("wrong event class name: " + typeName);
        }
        return clazz.isAssignableFrom(event.getClass());
    }
}
