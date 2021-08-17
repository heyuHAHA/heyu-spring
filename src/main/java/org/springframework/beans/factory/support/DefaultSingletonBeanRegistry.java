package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultSingletonBeanRegistry  implements SingletonBeanRegistry {
    private final Map<String,Object> singleonObjects = new HashMap<>();

    protected final Map<String,DisposableBean> disposableBeanMap = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return singleonObjects.get(beanName);
    }

    public void addSingleton(String beanName, Object singletonObject) {
        singleonObjects.put(beanName,singletonObject);
    }

    public void registerDisposableBean(String beanName, DisposableBean d) {
        disposableBeanMap.put(beanName,d);
    }


}
