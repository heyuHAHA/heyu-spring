package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

import java.beans.Beans;

public interface BeanFactory {

    /**
     * 获取Bean
     * @param name
     * @return
     * @throws BeansException
     */
    Object getBean(String name) throws BeansException;

    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

}
