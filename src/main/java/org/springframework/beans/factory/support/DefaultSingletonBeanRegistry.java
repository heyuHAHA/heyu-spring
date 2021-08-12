package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

public class DefaultSingletonBeanRegistry  implements SingletonBeanRegistry {
    private Map<String,Object> singleonObjects = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return null;
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        singleonObjects.put(beanName,singletonObject);
    }
}
