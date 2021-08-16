package org.springframework.beans.factory.support;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;

public class DisposableBeanAdapter implements DisposableBean {
    private final Object bean;
    private final String beanName;
    private final String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestoryMethodName();
    }



    @Override
    public void destroy() throws Exception {
        if (bean instanceof DisposableBean) {
            ((DisposableBean)bean).destroy();
        }
        if(StrUtil.isNotEmpty(destroyMethodName) && !(bean instanceof DisposableBean && "destroy".equals(this.destroyMethodName))) {
            Method destroyMethod = ClassUtil.getPublicMethod(bean.getClass(),destroyMethodName);
            if (destroyMethod != null) {
                destroyMethod.invoke(bean);
            }
        }
    }




}
