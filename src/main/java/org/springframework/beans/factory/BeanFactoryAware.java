package org.springframework.beans.factory;

public interface BeanFactoryAware extends Aware{
    void setBeanFactory(BeanFactory factory);
}
