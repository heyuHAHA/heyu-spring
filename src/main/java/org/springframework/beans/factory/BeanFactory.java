package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

public interface BeanFactory {

    /**
     * 获取Bean
     * @param name
     * @return
     * @throws BeansException
     */
    Object getBean(String name) throws BeansException;

}
