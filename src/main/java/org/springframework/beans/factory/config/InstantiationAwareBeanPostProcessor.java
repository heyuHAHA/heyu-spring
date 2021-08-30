package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor{
   Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;


   /**
    * 在bean实例化之后，设置属性
    * @param pvs
    * @param bean
    * @param beanName
    * @return
    * @throws BeansException
    */
   PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName)  throws BeansException;

   /**
    * 在实例化之后，设置属性之前执行
    */
   boolean postProcessAfterInstantiation(Object bean, String beanName);
}
