package org.springframework.context;

import org.springframework.beans.BeansException;

import java.beans.Beans;

public interface ConfigurableAppliationContext extends ApplicationContext{

    /**
     * 刷新容器
     * @throws BeansException
     */
    void refresh() throws BeansException;
}
